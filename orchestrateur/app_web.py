from flask import Flask, render_template, request, jsonify
import requests

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/catalog', methods=['GET'])
def get_catalog():
    try:
        res = requests.get("http://localhost:8080/api/inventory/catalog")
        return jsonify(res.json())
    except:
        return jsonify({"error": "Service Java injoignable"}), 500

@app.route('/commander', methods=['POST'])
def commander():
    data = request.json
    product_id = data.get('product_id')
    amount = data.get('amount')
    logs = [f"Commande reçue pour {product_id}"]

    # Etape 1. Vérification & Réservation (Java)
    try:
        inv_res = requests.get(f"http://localhost:8080/api/inventory/{product_id}").json()
        if not inv_res.get('isAvailable'):
            return jsonify({"success": False, "message": "Rupture de stock !"})
        logs.append("Étape 1 : Produit disponible, stock décrémenté dans le JSON.")
    except:
        return jsonify({"success": False, "message": "Service Inventaire Java injoignable"})

    # Etape 2. Paiement (Python)
    try:
        pay_res = requests.post("http://localhost:5000/api/payment", json={"amount": amount}).json()
        if pay_res.get('status') == 'success':
            logs.append(f"Étape 2 : Paiement validé (ID: {pay_res['transaction_id']})")
            return jsonify({"success": True, "logs": logs})
    except:
        return jsonify({"success": False, "message": "Service Paiement Python injoignable"})

    return jsonify({"success": False, "message": "Erreur système."})

if __name__ == '__main__':
    app.run(port=3000, debug=True)