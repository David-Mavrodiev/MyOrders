$('.list-group-item-success').on('click', function(event) {
    let element = $(event.target).children();
    let username = element.text();

    let body = {
        organizationName: username
    };

    $.ajax({
        url: "/chat",
        type: "get",
        cors: true,
        contentType: "application/json",
        data: JSON.stringify(body)
    });
});