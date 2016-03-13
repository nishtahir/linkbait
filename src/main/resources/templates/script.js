  function timeSince(timeStamp) {
    var now = new Date(),
      secondsPast = (now.getTime() - timeStamp.getTime()) / 1000;
    if(secondsPast < 60){
      return parseInt(secondsPast) + 's ago';
    }
    if(secondsPast < 3600){
      return parseInt(secondsPast/60) + 'm ago';
    }
    if(secondsPast <= 86400){
      return parseInt(secondsPast/3600) + 'h ago';
    }
    if(secondsPast > 86400){
        day = timeStamp.getDate();
        month = timeStamp.toDateString().match(/ [a-zA-Z]*/)[0].replace(" ","");
        year = timeStamp.getFullYear() == now.getFullYear() ? "" :  " "+timeStamp.getFullYear();
        return day + " " + month + year;
    }
  }

  window.onload = function (){
    var elements = document.getElementsByClassName("time_since");
    for(var i=0; i<elements.length; i++) {
        elements[i].innerHTML = timeSince(new Date(elements[i].getAttribute("timestamp")*1000));
    }
  }