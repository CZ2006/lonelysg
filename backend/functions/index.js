const functions = require('firebase-functions')
const admin = require('firebase-admin')
const express = require('express')

const apirouteIris = require('./routes/Iris/api')
const apirouteMinHui = require('./routes/MinHui/api')
const apirouteCheyanne = require('./routes/Cheyanne/api')
const apirouteWinnie = require('./routes/Winnie/api')
const apirouteXQ = require('./routes/XQ/api')

const app = express();
app.use(require('cors')({ origin: true, credentials: true }))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))

// var serviceAccount = require("../ignore/lonely-4a186-firebase-adminsdk-xiwbf-eb81a9c382.json")
admin.initializeApp({
    // credential: admin.credential.cert(serviceAccount),
    credential: admin.credential.applicationDefault(),
    databaseURL: "https://lonely-4a186.firebaseio.com/"
});

var db = admin.database();
app.set('database',db)
app.use('/Iris', apirouteIris) //Route to Iris' functions
app.use('/MinHui', apirouteMinHui) //Route to Min Hui's functions
app.use('/Cheyanne', apirouteCheyanne)
app.use('/Winnie', apirouteWinnie)
app.use('/XQ', apirouteXQ)
exports.app = functions.https.onRequest(app)


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
