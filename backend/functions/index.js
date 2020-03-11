const functions = require('firebase-functions')
const admin = require('firebase-admin')
const express = require('express')
const apiroute = require('./routes/api')

const app = express();
app.use(require('cors')({ origin: true, credentials: true }))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))

var serviceAccount = require("../ignore/lonely-4a186-firebase-adminsdk-xiwbf-361c914115.json")
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    
    databaseURL: "https://lonely-4a186.firebaseio.com/"
});

var db = admin.database();
app.set('database',db)
app.use('/api', apiroute)
exports.app = functions.https.onRequest(app)


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
