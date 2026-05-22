from flask import Flask, request, jsonify
import uuid

app = Flask(__name__)

@app.route('/api/payment', methods=['POST'])
def process_payment():
    data = request.get_json()
    amount = data.get('amount')
    
    print(f"Demande de paiement reçue pour le montant : {amount}$")
    
    if amount and amount > 0:
        tx_id = str(uuid.uuid4())
        print(f"Paiement validé. ID de transaction généré : {tx_id}")
        return jsonify({
            "status": "success",
            "transaction_id": tx_id,
            "message": f"Paiement de {amount}$ validé."
        }), 200
    else:
        print("Paiement refusé : montant invalide.")
        return jsonify({
            "status": "error",
            "message": "Montant invalide ou manquant."
        }), 400

if __name__ == '__main__':
    app.run(port=5000, debug=True)