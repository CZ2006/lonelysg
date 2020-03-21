const express = require("express");
const router = express.Router();

router.get("/getUsers", (req,res)=>{ //Sample function fetching data from the realtime DB on firebase
    //Call using http://localhost:5001/lonely-4a186/us-central1/app/Iris/getStuff after running firebase serve
    let database = req.app.get("database")
    var ref = database.ref('User');
    ref.once("value", function(snapshot){
        console.log(snapshot.val()); //View response value in the command line
        res.end("OK Iris!") //Returned in the browser or postman
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

router.delete("/deleteUser", (req, res) => {

    let database = req.app.get("database")
    let userToDel = database.ref("User/User" + req.body)

    userToDel.set({})

    res.end(req.body + " deleted from the DB!")

})


router.post("/updateUser", (req, res) => {

    let database = req.app.get("database")
    let userToUpdate = database.ref("User/User" + req.body.userID)

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

module.exports = router;

//Todo

// - getUser
// - addUser
// - removeUser
// - editUser

// - addInvitation
// - removeInvitation
// - getInvitation
// - editInvitation