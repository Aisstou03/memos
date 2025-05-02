document.addEventListener('DOMContentLoaded', () => {
    const themeIcon = document.getElementById('theme-icon');
    const body = document.body;
    const themeIcons = document.querySelectorAll('.dropdown .icon');

    themeIcon.addEventListener('click', () => {
        themeIcon.classList.toggle('clicked');
    });

    // Ajouter des événements de clic pour chaque icône de thème
    themeIcons.forEach(icon => {
        icon.addEventListener('click', (e) => {
            const theme = e.currentTarget.getAttribute('data-theme'); // Utiliser currentTarget pour cibler le bon élément
            console.log(`Changement de thème : ${theme}`);  // Debugging
            changeTheme(theme);
        });
    });

    // Fonction pour changer le thème
    function changeTheme(theme) {
        console.log(`Activating theme: ${theme}`);  // Debugging
        body.classList.remove('light-theme', 'dark-theme', 'auto-theme');  // Retirer toutes les classes de thème
        if (theme === 'light') {
            body.classList.add('light-theme');
        } else if (theme === 'dark') {
            body.classList.add('dark-theme');
        } else if (theme === 'auto') {
            body.classList.add('auto-theme');
        }
    }

    // Initialiser le thème basé sur la préférence du système
    if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
        body.classList.add('dark-theme');
        console.log('Système en mode sombre');
    } else {
        body.classList.add('light-theme');
        console.log('Système en mode clair');
    }
});



document.addEventListener('DOMContentLoaded', () => {
    const toggleButtons = document.querySelectorAll('.toggle-btn');
    toggleButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Cibler l'élément des sous-sections correspondant au bouton
            const target = document.getElementById(button.getAttribute('data-target'));

            // Toggle l'affichage des sous-sections
            target.classList.toggle('open');

            // Toggle la transformation du bouton (changer de + à croix)
            button.classList.toggle('open');
        });
    });
});

function scrollSection(direction) {
  const section = document.querySelector('.scrollable-section');

  if (direction === 'up') {
    section.scrollBy({ top: -50, behavior: 'smooth' }); // Défilement vers le haut
  } else if (direction === 'down') {
    section.scrollBy({ top: 50, behavior: 'smooth' }); // Défilement vers le bas
  }
}

document.addEventListener('DOMContentLoaded', function () {
  const text = "Bibliothèque de l'Université Assane Seck de Ziguinchor (UASZ)";
  const typingTextElement = document.getElementById('typing-text');
  let index = 0;

  // Fonction pour afficher une lettre à la fois
  function typeLetter() {
    if (index < text.length) {
      typingTextElement.innerHTML += text.charAt(index); // Ajouter la lettre une par une
      index++;
      setTimeout(typeLetter, 100); // Attendre 100ms avant d'afficher la lettre suivante
    }
  }

  typeLetter();  // Lancer l'animation du texte
});

 function toggleSidebar(id) {
                    const sidebar = document.getElementById(id);
                    sidebar.style.display = sidebar.style.display === 'block' ? 'none' : 'block';
                  }

                  function logout() {
                    alert('Déconnexion réussie !');
                    // Remplacez par la logique de déconnexion réelle ou une redirection.
                    window.location.href = '/logout';
                  }

                  window.onclick = function (event) {
                    const userSidebar = document.getElementById('sidebar-user');
                    const notificationSidebar = document.getElementById('sidebar-notification');

                    if (!event.target.closest('.user-icon') && userSidebar) {
                      userSidebar.style.display = 'none';
                    }
                    if (!event.target.closest('.notification-icon') && notificationSidebar) {
                      notificationSidebar.style.display = 'none';
                    }
                  };

  document.getElementById('confirmButton').addEventListener('click', function () {
    const year = document.getElementById('yearInput').value;
    if (year) {
      alert(`Vous avez confirmé l'année : ${year}`);
      // Ajoutez ici le code pour télécharger ou effectuer l'action souhaitée
      const modal = bootstrap.Modal.getInstance(document.getElementById('downloadModal'));
      modal.hide();
    } else {
      alert('Veuillez entrer une année avant de confirmer.');
    }
  });




function initCharts(stats, annees, licenceCounts, masterCounts, doctoratCounts) {
    // Graphique en barre des mémoires par type
     console.log("Données injectées pour les graphiques :", {
            stats,
            annees,
            licenceCounts,
            masterCounts,
            doctoratCounts,
        });

  //diagrame en barre avec courbe
  const statsChartCtx = document.getElementById('statsChart').getContext('2d');
  new Chart(statsChartCtx, {
      type: 'bar',
      data: {
          labels: ['Licence', 'Master', 'Doctorat'],
          datasets: [
              {
                  label: 'Nombre de Mémoires',
                  data: stats,  // Les données des mémoires
                  backgroundColor: 'rgba(27, 154, 118, 0.6)', // Vert clair translucide
                  borderColor: '#1b9a76', // Contour des barres
                  borderWidth: 1,
                  barThickness: 30 // Épaisseur des barres
              },
              {
                  label: 'Tendance',
                  data: stats, // Les mêmes données, utilisées pour la courbe
                  type: 'line',
                  borderColor: '#004d40', // Une couleur sombre pour la courbe
                  tension: 0.4, // Lissage de la courbe
                  pointBackgroundColor: '#004d40', // Couleur des points sur la courbe
                  pointRadius: 4, // Taille des points
                  fill: false // Désactive le remplissage
              }
          ]
      },
      options: {
          responsive: true,
          maintainAspectRatio: true,  // Conserver un rapport d'aspect constant
          plugins: {
              legend: {
                  position: 'top',
              },
              tooltip: {
                  callbacks: {
                      label: function(tooltipItem) {
                          return tooltipItem.raw + ' mémoires';
                      }
                  }
              }
          },
          scales: {
              x: {
                  beginAtZero: true
              },
              y: {
                  beginAtZero: true
              }
          }
      }
  });

//diagrammes circulaires des types de memoires
const pieChartCtx = document.getElementById('pieChart').getContext('2d');
new Chart(pieChartCtx, {
    type: 'pie',
    data: {
        labels: ['Licence', 'Master', 'Doctorat'],
        datasets: [
            {
                data: stats,  // Les données des mémoires
                backgroundColor: [
                    '#98FF98', // Vert clair
                    '#1b9a76', // Vert moyen
                    '#004d40'  // Vert sombre
                ],
                borderColor: '#ffffff', // Bordure blanche pour une meilleure séparation
                borderWidth: 2
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,  // Garder un rapport d'aspect constant
        plugins: {
            legend: {
                position: 'top',
            },
            tooltip: {
                callbacks: {
                    label: function(tooltipItem) {
                        const label = tooltipItem.label;
                        const value = tooltipItem.raw;
                        return `${label}: ${value} mémoires`;
                    }
                }
            }
        }
    }
});

    // Graphique linéaire de l'évolution des mémoires par année
    const evolutionChartCtx = document.getElementById('evolutionCharts').getContext('2d');
    new Chart(evolutionChartCtx, {
        type: 'line',
        data: {
            labels: annees,  // Utiliser les années
            datasets: [
                {
                    label: 'Licence',
                    data: licenceCounts,  // Nombre de mémoires de Licence par année
                     borderColor: '#98FF98', // Vert clair pour la courbe
                     fill: false,
                    tension: 0.4 // Courbe plus lissée
                },
                {
                    label: 'Master',
                    data: masterCounts,  // Nombre de mémoires de Master par année
                    borderColor: '#1b9a76', // Une nuance de vert un peu plus foncée pour Master
                    fill: false,
                    tension: 0.4
                },
                {
                    label: 'Doctorat',
                    data: doctoratCounts,  // Nombre de mémoires de Doctorat par année
                    borderColor: '#004d40', // Une couleur encore plus foncée pour Doctorat
                    fill: false,
                    tension: 0.4
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            return tooltipItem.raw + ' mémoires';
                        }
                    }
                }
            },
            scales: {
                x: {
                    beginAtZero: true
                }
            }
        }
    });
}
//fonction pour les trois points
function toggleDropdown(icon, event) {
  event.stopPropagation(); // ✅ maintenant ça fonctionne

  // Fermer tous les autres dropdowns d'abord
  document.querySelectorAll('.dropdown-content').forEach(el => el.style.display = 'none');

  // Trouver le dropdown correspondant à l'icône cliquée
  const dropdown = icon.nextElementSibling;

  // Afficher ou masquer ce dropdown
  dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
}
document.querySelectorAll('.dropdown-content').forEach(menu => {
  menu.addEventListener('click', function (e) {
    e.stopPropagation();
  });
});
document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll('.dropdown-content').forEach(menu => {
    menu.addEventListener('click', function (e) {
      e.stopPropagation();
    });
  });

  // Fermer les menus si on clique ailleurs
  document.addEventListener('click', function () {
    document.querySelectorAll('.dropdown-content').forEach(el => el.style.display = 'none');
  });
});


