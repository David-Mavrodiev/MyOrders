$("#addOrder").on("click", (evt) => {
    let title = ($("#ProductTitle").text()).split(' ')[0];
    console.log(title);

    let body = {
        title: title
    }

    $.ajax({
            url: "/addOrder",
            type: "post",
            cors: true,
            contentType: "application/json",
            data: JSON.stringify(body),
            success: function(response) {
                location.reload();
            }
    });
})