let selectedStudentId = null;
let actionType = "";

function openModal(studentId, type) {
  selectedStudentId = studentId;
  actionType = type;

  document.getElementById("modal-title").innerText =
    type === "admit" ? "Admit Student to a School" : "Transfer Student";

  document.getElementById("modal").style.display = "flex";
}

function closeModal() {
  document.getElementById("modal").style.display = "none";
}

function confirmAction() {
  let schoolId = document.getElementById("school-select").value;
  let url = `http://localhost:8080/students/${selectedStudentId}/${actionType}`;

  fetch(url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ schoolId }),
  }).then((response) => {
    closeModal();
    htmx.ajax("GET", "http://localhost:8080/students/", { target: "#content" }); // Refresh student list
  });
}

function openAddModal() {
  let activeTab = document.querySelector(".navbar .active").innerText;

  if (activeTab === "Students") {
    document.getElementById("modalTitle").innerText = "Add New Student";
    document.getElementById("schoolFields").style.display = "none";
  } else {
    document.getElementById("modalTitle").innerText = "Add New School";
    document.getElementById("schoolFields").style.display = "flex";
  }

  document.getElementById("addModal").style.display = "flex";
}

function closeAddModal() {
  document.getElementById("addModal").style.display = "none";
}

async function submitForm(event) {
  event.preventDefault(); // Prevent default form submission

  let activeTab = document.querySelector(".navbar .active").innerText;

  let formType = activeTab === "Students" ? "student" : "school";
  const url =
    formType === "school"
      ? "http://localhost:8080/schools/add"
      : "http://localhost:8080/students/add";

  const formData = new FormData(event.target);
  const jsonObject = {};
  formData.forEach((value, key) => (jsonObject[key] = value));

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(jsonObject),
    });

    if (response.ok) {
      closeAddModal(); // Close modal on success
      const listUrl =
        formType === "school"
          ? "http://localhost:8080/schools/"
          : "http://localhost:8080/students/";

      htmx.ajax("GET", listUrl, { target: "#content" }); // Refresh the list
    } else {
      console.error("Error:", await response.text());
    }
  } catch (error) {
    console.error("Network error:", error);
  }
}
