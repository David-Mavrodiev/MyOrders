var button = document.getElementById('submit-button');
button.addEventListener('click', function(ev) {
    var number = document.getElementById("number");
    var container = document.getElementsByClassName('container')[0];
    container.setAttribute("style", "margin: 0 0;padding: 0 0;width: 100%;height:30em;");
    container.innerHTML = "<div class=\" text-center\"><h2>Отговор на сигнал №" + number.value + "</h2></div>";
    console.log(number.value)
    let body = {
        registeredNumber: number.value
    };

    $.ajax({
        url: "",
        type: "post",
        cors: true,
        contentType: "application/json",
        data: JSON.stringify(body),
        success: function(response) {
            console.log(response);
            let p = document.createElement("h3");
            p.style.textAlign = "center";
            p.innerHTML = response;
            container.appendChild(p);
        }
    });
});