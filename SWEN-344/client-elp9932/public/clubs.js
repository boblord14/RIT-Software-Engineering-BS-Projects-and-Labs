var arcaneCount = 0;
var undergroundCount = 0;
var sodaCount = 0;
var studioCount = 0;

function addMember() {   
    var clubButtons = document.getElementsByName('clubs');
    var counts = document.getElementsByName('count');

    if(clubButtons[0].checked) {
        //arcane
        if(arcaneCount != 100){
            arcaneCount = arcaneCount + 1;
        }
        counts[0].textContent = arcaneCount;
        adjColorCapacity(document.getElementById('arcane'), document.getElementById('arcaneStatus'), arcaneCount, 100, 70);
      }else if(clubButtons[1].checked) {
        //underground
        if(undergroundCount != 50){
            undergroundCount = undergroundCount + 1;
        }
        counts[1].textContent = undergroundCount;
        adjColorCapacity(document.getElementById('underground'), document.getElementById('undergroundStatus'), undergroundCount, 50, 30);

      }else if(clubButtons[2].checked) {
        //soda
        if(sodaCount != 20){
            sodaCount = sodaCount + 1;
        }
        counts[2].textContent = sodaCount;
        adjColorCapacity(document.getElementById('soda'), document.getElementById('sodaStatus'), sodaCount, 20, 12);
      }else if(clubButtons[3].checked) {
        //studio
        if(studioCount != 52){
            studioCount = studioCount + 1;
        }
        counts[3].textContent = studioCount;
        adjColorCapacity(document.getElementById('studio'), document.getElementById('studioStatus'), studioCount, 52, 32);
      }
}

function removeMember() {   
    var clubButtons = document.getElementsByName('clubs');
    var counts = document.getElementsByName('count');

    if(clubButtons[0].checked) {
        //arcane
        if(arcaneCount != 0){
            arcaneCount = arcaneCount - 1;
        }
        counts[0].textContent = arcaneCount;
        adjColorCapacity(document.getElementById('arcane'), document.getElementById('arcaneStatus'), arcaneCount, 100, 70)
      }else if(clubButtons[1].checked) {
        //underground
        if(undergroundCount != 0){
            undergroundCount = undergroundCount - 1;
        }
        counts[1].textContent = undergroundCount;
        adjColorCapacity(document.getElementById('underground'), document.getElementById('undergroundStatus'), undergroundCount, 50, 30);
      }else if(clubButtons[2].checked) {
        //soda
        if(sodaCount != 0){
            sodaCount = sodaCount - 1;
        }
        counts[2].textContent = sodaCount;
        adjColorCapacity(document.getElementById('soda'), document.getElementById('sodaStatus'), sodaCount, 20, 12);
      }else if(clubButtons[3].checked) {
        //studio
        if(studioCount != 0){
            studioCount = studioCount - 1;
        }
        counts[3].textContent = studioCount;
        adjColorCapacity(document.getElementById('studio'), document.getElementById('studioStatus'), studioCount, 52, 32);
      }
}

function adjColorCapacity(box, status, count, max, yellow){
    if(count === 0){
      box.style.backgroundColor = "LightGreen";
      status.textContent = "";
    } else if(count<yellow){
        box.style.backgroundColor = "LightGreen";
        status.textContent = "Welcome!";
    } else if(count>=max){
        box.style.backgroundColor = "Red";
        status.textContent = "No one allowed in!";
    } else{
        box.style.backgroundColor = "Yellow";
        status.textContent = "Warn the bouncers…";
    }
}
