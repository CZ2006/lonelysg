const express = require("express");
const router = express.Router();

// === User features ===

//Open: How do we link user profiles with authentication implemented by Winnie?

router.get("/getUser/:userID", (req,res) => {
    let database = req.app.get("database")
    var targetUser = database.ref('User/User' + req.param('userID'));
    targetUser.once("value", function(snapshot){
        res.end(JSON.stringify(snapshot.val()));
    })
})


router.post("/addUser", (req, res) => { 
    let database = req.app.get("database")
    let userRef = database.ref("User")

    const newUser = userRef.child("User" + req.body.userID) //Requires information sent in JSON format and containing at least userID
    newUser.set(req.body)

    res.end("User" + req.body.userID + " added inside the DB!"); //Returned in postman

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

router.delete("/deleteUser/:userID", (req, res) => {

    let database = req.app.get("database")
    let userToDel = database.ref('User/User' + req.param('userID'))
    userToDel.set({})

    res.end("deleted from the DB!")

})


router.post("/updateUser", (req, res) => {

    let database = req.app.get("database")
    let userToUpdate = database.ref("User" + req.body.userID)

    async function updateUser() {
        var newData;

        await userToUpdate.once("value", function(snapshot){
            newData = snapshot.val()
        })

        for (key in req.body) {
            newData[key] = req.body[key]
        }
    
        userToUpdate.set(newData)
    
        res.end("User" + req.body.userID + " updated!")
    }

    updateUser()

    /* Sample data to test this function:

    {
        "userID": 2,
        "password": "newPa55w0rd...",
        "interests": "Youtube, Marvel"
    } */

})

// === Invitation features ===

/* router.get("/addInvitation", (req, res) => {

    res.end("Invitation added!")
}) */

module.exports = router;

//Todo

// - addInvitation
// - removeInvitation
// - getInvitation
// - editInvitation