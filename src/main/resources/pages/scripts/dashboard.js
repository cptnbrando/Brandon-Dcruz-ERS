'use strict';

// On load for both managers and employees we immediately ping db for use and reimbursement data
// api/reimbursements returns user specific info for employees or all reimbursements for managers
window.onload = async () => {
    let reimbReq = await fetch(`http://localhost:9002/api/reimbursements`);
    let reimbursements = await reimbReq.json();

    let userResponse = await fetch(`http://localhost:9002/api/user`);
    let user = await userResponse.json();

    loadTable(reimbursements, "#reimbursementTableBody");
    makeUI(user);
}

// Creates a new row for each reimbursement from a json of reimbursement objects
function loadTable(reimbursements, tableBodyID)
{
  let tableBody = document.querySelector(tableBodyID);

  let theTable = "";

  if(reimbursements.length === 0)
  {
    theTable += `<tr><h4>No pending requests</h4></tr>`
  }
  else
  {
    reimbursements.forEach(element => 
    {
      let tableRow = 
      `<tr val=${element.reimbID}>
          <th scope="row">${new Date(element.dateSubmitted).toISOString().substring(0, 10)}</th>
          <td>${element.authorFirstName} ${element.authorLastName} : ${element.authorUsername}</td>
          <td>${element.type}</td>
          <td>${element.description}</td>
          <td>\$${element.amount}</td>
          <td>${element.status}</td>`;
      
      // If it's the pending table, add the approve/deny buttons
      if(tableBodyID === "#pendingTableBody")
      {
        tableRow += 
        `<td>
          <div class="btn-group" role="group" aria-label="Approve/Deny" pressed="no" val=${element.reimbID}>
            <button type="button" class="btn btn-success approveButt">Approve</button>
            <button type="button" class="btn btn-danger denyButt">Deny</button>
          </div>
        </td>`;
      }

      tableRow += `</tr>`;
      theTable += tableRow;
    });
  }

  tableBody.innerHTML = theTable;

  // If it's the pending table, add the event listeners for every approve / deny button
  if(tableBodyID === "#pendingTableBody")
  {
    document.querySelectorAll(".approveButt").forEach((butt) => {
      butt.addEventListener("click", approve);
    });

    document.querySelectorAll(".denyButt").forEach((butt) => {
      butt.addEventListener("click", deny);
    });
  }
}

// Sets the side nav for managers
function makeManagerUI()
{
  document.querySelector("#sideNavList").innerHTML = `<li class="nav-item" id="sideItem1">
                            <a class="nav-link active" href="#reimbursements">
                                <span data-feather="home"></span>
                                <span href="#reimbursements" id="sideItem1Label">All Reimbursements</span>
                            </a>
                        </li>
                        <li class="nav-item" id="sideItem2">
                            <a class="nav-link" href="#pending">
                              <span data-feather="alert-triangle"></span>
                              <span href="#pending" id="sideItem2Label">Update Pending Requests</span>
                          </a>
                        </li>`;

  document.querySelector("#mainLabel").innerText = "All Reimbursements";
}

// Sets the side nav for employees
function makeEmployeeUI()
{
  document.querySelector("#sideNavList").innerHTML = `<li class="nav-item" id="sideItem1">
                          <a class="nav-link active" href="#reimbursements">
                              <span data-feather="home"></span>
                              <span href="#reimbursements" id="sideItem1Label">My Reimbursement Requests</span>
                          </a>
                      </li>
                      <li class="nav-item" id="sideItem2">
                          <a class="nav-link" href="#newReimbursement">
                              <span data-feather="file"></span>
                              <span href="#newReimbursement" id="sideItem2Label">Create New Reimbursement</span>
                          </a>
                      </li>`;

  document.querySelector("#mainLabel").innerText = "My Reimbursement Requests";
}

// Prints the name at the top and sets the tables as JQuery Data Tables
function makeUI(user)
{
  document.querySelector("#welcomeText").innerText = ` - Hello ${user.firstName}!`;

  if(user.role === "Manager")
  {
    makeManagerUI();

    $("#pendingTable").DataTable({
      "pageLength": 10,
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": [1, 'asc'],
      "info": true,
      "autoWidth": false
    });
  }
  else
  {
    makeEmployeeUI();
  }

  $("#reimbursementTable").DataTable({
    "pageLength": 10,
    "paging": true,
    "lengthChange": false,
    "searching": false,
    "ordering": [1, 'asc'],
    "info": true,
    "autoWidth": true
  });

  // For every nav button, set the event listener
  document.querySelectorAll(".nav-item").forEach((navItem) => {
    navItem.addEventListener("click", sidenavAction);
  });

  feather.replace();
}

// Every nav button calls this event on click
async function sidenavAction(e)
{
  if(e.target.innerText === "My Reimbursement Requests" || e.target.innerText === "All Reimbursements")
  {
    window.location.replace("http://localhost:9002/dashboard");
  }

  // Remove the .active class from every link to make them all not blue
  document.querySelectorAll(".nav-link").forEach((navLink) => {
    navLink.classList.remove("active");
  });

  // Set the display to none for every main page element
  document.querySelectorAll(".mainPage").forEach((page) => {
    page.style.display = "none";
  });

  // Set the label at the top of the screen to the chosen menu
  document.querySelector("#mainLabel").innerText = e.target.innerText;

  // Make the target nav link blue
  e.target.parentNode.classList.add("active");

  // Display the corresponding main page element
  let pageID = e.target.attributes.href.nodeValue;
  document.querySelector(pageID).style.display = "block";

  // Refresh the corresponding table
  if(e.target.innerText === "Update Pending Requests")
  {
    let pendingReq = await fetch(`http://localhost:9002/api/reimbursements/pending`);
    let pendingReimbursements = await pendingReq.json();

    loadTable(pendingReimbursements, "#pendingTableBody");
  }
}

// Approve buttons will call this
async function approve(e)
{
  // ID of table value is stored in parent btn-group for both buttons
  let reimbID = e.target.parentNode.attributes.val.value;
  let wasPressed = e.target.parentNode.attributes.pressed.value;

  // If statement prevents spam/accidental multi clicks
  if(wasPressed === "no")
  {
    e.target.parentNode.attributes.pressed.value = "yes";
    let response = await fetch(`http://localhost:9002/api/reimbursements/${reimbID}/approve`, {
      method: "PATCH",
      'headers': {
        'Content-Type': 'application/json'
      }
    }).catch(
      (stuff) => {
        console.log("this sucker exploded");
        console.log(stuff);
      }
    )

    let pendingReq = await fetch(`http://localhost:9002/api/reimbursements/pending`);
    let pendingReimbursements = await pendingReq.json();

    loadTable(pendingReimbursements, "#pendingTableBody");
  }
}

// Deny buttons will call this
async function deny(e)
{
  let reimbID = e.target.parentNode.attributes.val.value;
  let wasPressed = e.target.parentNode.attributes.pressed.value;

  if(wasPressed === "no")
  {
    e.target.parentNode.attributes.pressed.value = "yes";
    let response = await fetch(`http://localhost:9002/api/reimbursements/${reimbID}/deny`, {
      method: "PATCH",
      'headers': {
        'Content-Type': 'application/json'
      }
    }).catch(
      (stuff) => {
        console.log("this sucker exploded");
        console.log(stuff);
      }
    )

    let pendingReq = await fetch(`http://localhost:9002/api/reimbursements/pending`);
    let pendingReimbursements = await pendingReq.json();

    loadTable(pendingReimbursements, "#pendingTableBody");
  }
}

// function addDummyData() {
//   let fakeData = [
//     {
//       amount: 23749,
//       authorID: 19283218391,
//       dateResolved: 1613593529000,
//       dateSubmitted: 1613593529000,
//       description: "Testing 1",
//       reimbID: 83829274,
//       resolverID: 83927472,
//       status: "DENIED",
//       statusAsInt: 3,
//       type: "OTHER",
//       typeAsInt: 2
//     },
//     {
//       amount: 23749,
//       authorID: 19283218391,
//       dateResolved: 1613593529000,
//       dateSubmitted: 1613593529000,
//       description: "Testing 1",
//       reimbID: 83829274,
//       resolverID: 83927472,
//       status: "DENIED",
//       statusAsInt: 3,
//       type: "OTHER",
//       typeAsInt: 2
//     },
//     {
//       amount: 23749,
//       authorID: "hTejd",
//       dateResolved: 1613593529000,
//       dateSubmitted: 1613593529000,
//       description: "Testing 1",
//       reimbID: 83829274,
//       resolverID: 83927472,
//       status: "DENIED",
//       statusAsInt: 3,
//       type: "OTHER",
//       typeAsInt: 2
//     },
//     {
//       amount: 23749,
//       authorID: "Brando",
//       dateResolved: 1613593529000,
//       dateSubmitted: 1613593529000,
//       description: "Testing 1",
//       reimbID: 83829274,
//       resolverID: 83927472,
//       status: "DENIED",
//       statusAsInt: 3,
//       type: "OTHER",
//       typeAsInt: 2
//     }
//   ];

//   loadDash(fakeData);
// }
