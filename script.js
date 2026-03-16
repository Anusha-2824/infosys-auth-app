// ===============================
// API URL
// ===============================
const API = "http://localhost:8080";


// ===============================
// LOGIN
// ===============================
async function signin(){

const username = document.getElementById("si-username").value;
const password = document.getElementById("si-password").value;
const msg = document.getElementById("signin-msg");

msg.innerText="";

try{

const response = await fetch(API + "/api/auth/login",{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
username:username,
password:password
})

});

if(!response.ok){

msg.innerText="Invalid username or password";
return;

}

const token = await response.text();

/* save JWT */

localStorage.setItem("token",token);

msg.style.color="lightgreen";
msg.innerText="Login successful";

/* redirect */

window.location.href="dashboard.html";

}catch(error){

console.error(error);
msg.innerText="Cannot connect to backend server";

}

}


// ===============================
// REGISTER
// ===============================
async function signup(){

const username = document.getElementById("su-username").value;
const password = document.getElementById("su-password").value;
const email = document.getElementById("su-email").value;
const role = document.getElementById("su-role").value;

const msg = document.getElementById("signup-msg");

msg.innerText="";

try{

const response = await fetch(API + "/api/auth/register?username="+username+
"&email="+email+
"&password="+password+
"&role="+role,{

method:"POST"

});

const data = await response.text();

msg.innerText=data;

}catch(error){

msg.innerText="Server error";

}

}


// ===============================
// LOGOUT
// ===============================
function logout(){

localStorage.removeItem("token");

window.location.href="index.html";

}