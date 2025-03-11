<!-- Responsive-->
var win = navigator.platform.indexOf('Win') > -1;
if (win && document.querySelector('#sidenav-scrollbar')) {
  var options = {
    damping: '0.5'
  }
  Scrollbar.init(document.querySelector('#sidenav-scrollbar'), options);
}

/*<![CDATA[*/  <!-- donnes comptes des memoires-->
var annees = /*[[${annees}]]*/ [];
var licencesCounts = /*[[${licencesCounts}]]*/ [];
var mastersCounts = /*[[${mastersCounts}]]*/ [];
var thesesCounts = /*[[${thesesCounts}]]*/ [];
/*]]>*/

<!-- Graphes-->
document.addEventListener("DOMContentLoaded", function () {
    var ctxLicence = document.getElementById("chart-licence").getContext("2d");
    var ctxMaster = document.getElementById("chart-master").getContext("2d");
    var ctxDoctorat = document.getElementById("chart-doctorat").getContext("2d");

    // Vérification des données injectées par Thymeleaf
    console.log("Années:", annees);
    console.log("Licences:", licencesCounts);
    console.log("Masters:", mastersCounts);
    console.log("Doctorats:", thesesCounts);

    // Graphique Licence
    new Chart(ctxLicence, {
        type: "bar",
        data: {
            labels: annees,
            datasets: [{
                label: "Nombre de Licences",
                data: licencesCounts,
                backgroundColor: "#43A047",
                borderWidth: 1,
                borderRadius: 4,
                borderSkipped: false,
                barThickness: 'flex'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: false, // Ne commence pas à 0
                    suggestedMin: 1, // Fixe 1 comme valeur minimale de l'axe Y
                    ticks: {
                        // Utilisation de `stepSize` pour garantir l'affichage des entiers
                        stepSize: 1, // Force l'affichage des entiers uniquement
                        callback: function(value) {
                            return Number.isInteger(value) ? value : ''; // Assure l'affichage d'entiers
                        }
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return context.dataset.label + ": " + context.raw;
                        }
                    }
                }
            }
        }
    });

   // Graphique Master
    new Chart(ctxMaster, {
        type: "line",
        data: {
            labels: annees,
            datasets: [{
                label: "Nombre de Masters",
                data: mastersCounts,
                pointRadius: 3,
                pointBackgroundColor: "#43A047",
                pointBorderColor: "transparent",
                borderColor: "#43A047",
                backgroundColor: "transparent",
                tension: 0.4, // Ajoute une courbe lissée
                borderWidth: 2,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: false, // Ne commence pas à 0
                    suggestedMin: 1, // Fixe 1 comme valeur minimale de l'axe Y
                    ticks: {
                        stepSize: 1, // Force l'affichage des entiers uniquement
                        callback: function(value) {
                            return Number.isInteger(value) ? value : ''; // Assure l'affichage d'entiers
                        }
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            return context.dataset.label + ": " + context.raw;
                        }
                    }
                }
            }
        }
    });


      // Graphique Doctorat
      new Chart(ctxDoctorat, {
          type: "bar",
          data: {
              labels: annees,
              datasets: [{
                  label: "Nombre de Doctorats",
                  data: thesesCounts,
                  tension: 0,
                  borderWidth: 2,
                  pointRadius: 3,
                  pointBackgroundColor: "#43A047",
                  pointBorderColor: "transparent",
                  borderColor: "#43A047",
                  backgroundColor: "transparent",
                  fill: true
              }]
          },
          options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: {
                  y: {
                      beginAtZero: false, // Ne commence pas à 0
                      suggestedMin: 1 // Fixe 1 comme valeur minimale de l'axe Y
                  }
              },
              plugins: {
                  tooltip: {
                      callbacks: {
                          label: function (context) {
                              return context.dataset.label + ": " + context.raw;
                          }
                      }
                  }
              }
          }
      });
});


function deleteRow(element) {
            element.closest("tr").remove();
}

function editMemoire(element) {
    let row = element.closest("tr");
    let data = [];
    row.querySelectorAll("td").forEach((td, index) => {
        if (index < 10) {
            data.push(td.innerText);
        }
    });

    localStorage.setItem("memoireToEdit", JSON.stringify(data));
    window.location.href = "modifierMemoireLicence.html";
}
