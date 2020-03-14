const express = require("express");
const router = express.Router();

router.get("/getStuff", (req,res)=>{ //Sample function fetching data from the realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/Iris/getStuff after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val());
        res.end("OK Iris!")
    })
})

router.post("/postUser", (req, res) => {

    let database = req.app.get("database")
    let userRef = database.ref("User")

    const newUser = userRef.child("User" + req.body.userID)
    newUser.set(req.body)

    res.end("Posted!");

    /* Sample data to test this function:
    
    {
        "userID": 2,
        "username": "RayRay",
        "password": "00ThisIs@GreatPassword!",
        "name": "Raymond",
        "email": "ray@email.com",
        "gender": "M",
        "birthday": "Undefined",
        "profilePic": "fantasticURL",
        "job": "NTU Student",
        "interests": "No idea"
    } */
})

module.exports = router;