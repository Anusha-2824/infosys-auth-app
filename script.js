// =====================================
// MOCK DATABASE (UserRepository)
// =====================================
const users = [];

// =====================================
// SESSION (Current Logged-in User)
// =====================================
let currentUser = null;
const e = suEmail.value;

users.push({
  username: u,
  password: p,
  role: r,
  email: e
});


// =====================================
// UTILITY FUNCTIONS
// =====================================
function encrypt(password) {
  return btoa(password); // simple encryption (demo purpose)
}

function generateToken(username) {
  return "JWT_" + username + "_" + Date.now();
}

// =====================================
// AUTH SERVICE (BUSINESS LOGIC)
// =====================================
const AuthService = {

  // -------- REGISTER USER --------
  registerUser(userDetails) {

    // EMPLOYEE CANNOT CREATE ADMIN
    if (currentUser && currentUser.role === "EMPLOYEE" && userDetails.role === "ADMIN") {
      return "Employees are not allowed to create admin accounts";
    }

    // ONLY ADMIN CAN CREATE USERS
    if (userDetails.role === "ADMIN" && (!currentUser || currentUser.role !== "ADMIN")) {
      return "Only admin can create admin accounts";
    }

    const existingUser = users.find(
      user => user.username === userDetails.username
    );

    if (existingUser) {
      return "User already exists";
    }

    const newUser = {
      username: userDetails.username,
      password: encrypt(userDetails.password),
      email: userDetails.email,
      role: userDetails.role,
      failedAttempts: 0,
      locked: false
    };

    users.push(newUser);
    return "User created successfully";
  },

  // -------- AUTHENTICATE USER --------
  authenticate(username, password) {

    const user = users.find(user => user.username === username);

    if (!user) {
      return "Invalid username";
    }

    if (user.locked) {
      return "Account locked. Contact admin.";
    }

    if (user.password === encrypt(password)) {
      user.failedAttempts = 0;
      currentUser = user;

      const token = generateToken(username);

      // ROLE-BASED REDIRECT
      if (user.role === "ADMIN") {
        window.location.href = "admin-dashboard.html";
      } else {
        window.location.href = "employee-dashboard.html";
      }

      return token;

    } else {
      user.failedAttempts++;

      if (user.failedAttempts >= 3) {
        user.locked = true;
        return "Account locked due to multiple failed attempts";
      }

      return `Invalid password (${user.failedAttempts}/3)`;
    }
  },

  // -------- FORGOT PASSWORD --------
  processForgotPassword(email) {
    const user = users.find(user => user.email === email);

    if (!user) {
      return "Email not registered";
    }

    return "Password reset link sent (mock)";
  }
};

// =====================================
// AUTH CONTROLLER (UI FUNCTIONS)
// =====================================
function signup() {
  const userDetails = {
    username: document.getElementById("su-username").value,
    password: document.getElementById("su-password").value,
    email: document.getElementById("su-email").value,
    role: document.getElementById("su-role").value
  };

  const result = AuthService.registerUser(userDetails);
  document.getElementById("signup-msg").innerText = result;
}

function signin() {
  const username = document.getElementById("si-username").value;
  const password = document.getElementById("si-password").value;

  const result = AuthService.authenticate(username, password);
  document.getElementById("signin-msg").innerText = result;
}

function forgotPassword() {
  const email = document.getElementById("fp-email").value;

  const result = AuthService.processForgotPassword(email);
  document.getElementById("forgot-msg").innerText = result;
}

// =====================================
// ADMIN FUNCTIONS
// =====================================
function createEmployee() {

  if (!currentUser || currentUser.role !== "ADMIN") {
    document.getElementById("admin-msg").innerText =
      "Access denied. Admin only.";
    return;
  }

  const employeeDetails = {
    username: document.getElementById("emp-username").value,
    password: document.getElementById("emp-password").value,
    email: document.getElementById("emp-email").value,
    role: "EMPLOYEE"
  };

  const result = AuthService.registerUser(employeeDetails);
  document.getElementById("admin-msg").innerText = result;
}

// =====================================
// LOGOUT
// =====================================
function logout() {
  currentUser = null;
  window.location.href = "index.html";
}
