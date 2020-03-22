const express = require("express");
const router = express.Router();

router.get("/getMessage", (req,res)=>{ 
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getMessage after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('Messages');
    ref.once("value", function(snapshot){
        console.log(snapshot.val()); //View response value in the command line
        res.end("Messages loaded.") //Returned in the browser or postman
    })
})

router.post("/sendMessage", (req, res) => { //Sample function adding new user to realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/sendMessage in postman with sample data in the body after running firebase serve
    let database = req.app.get("database")
    let userRef = database.ref("Messages")

    const newUser = userRef.child("Message" + req.body.userID) //Requires information sent in JSON format and containing at least userID
    newUser.set(req.body)

    res.end("Message sent."); //Returned in postman
})


router.get("/getStuff", (req,res)=>{ //Sample function fetching data from the realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/getStuff after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val()); //View response value in the command line
        res.end("OK Min Hui!") //Returned in the browser or postman
    })
})

router.post("/addUser", (req, res) => { //Sample function adding new user to realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/MinHui/addUser in postman with sample data in the body after running firebase serve
    let database = req.app.get("database")
    let userRef = database.ref("User")

    const newUser = userRef.child("User" + req.body.userID) //Requires information sent in JSON format and containing at least userID
    newUser.set(req.body)

    res.end("Posted!"); //Returned in postman

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

//Update user
//Delete user

module.exports = router;