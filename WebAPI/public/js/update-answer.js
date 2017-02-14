$("#button-send").on("click", function() {
    let registeredNumber = $("#number").text().split(' ')[2];
    let answer = $("#comment").val();

    let body = {
        registeredNumber: registeredNumber,
        answer: answer
    }

    $.ajax({
        url: "/signals",
        type: "post",
        cors: true,
        contentType: "application/json",
        data: JSON.stringify(body),
        success: function(response) {
            location.href = "/signals";
        }
    });
});