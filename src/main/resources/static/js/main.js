$(document).ready(function () {

    // get the server url
    let serverUrl = window.location.protocol + "//" + window.location.host;

    $(document).on('click', '.message-read', function (event) {

        $.ajax({
            method: 'DELETE',
            url: serverUrl + "/account/messages/read/" + $(this).data('id'),
            contentType: 'application/json',
            success: function () {
                queryUserMessages();
            }, error: function (errors) {
                console.log(errors);
            }
        });
    });

    $(".custom-file-input").on("change", function () {
        let fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });


    let loadShoppingCart = function () {
        $.ajax({
            method: 'GET',
            url: "/customer/shoppingCart",
            dataType: 'json',
            success: function (items) {
                if (items.length > 0) {
                    $('#cart-item-count').html(items.length);
                    let itemHtml = '';
                    $.each(items, function (i, item) {
                        itemHtml += `<tr>
                                        <td style="width: 100px">
                                            <a href="/book/${item.id}"><img src="${item.picture}" class="border-0 rounded-circle img-fluid img-thumbnail w-75" /></a>
                                        </td>
                                        <td>
                                            <div class="row">
                                                <span class="text-info font-italic"><a href="/book/${item.id}">${item.bookName}</a></span>
                                            </div>
                                            <div class="row">
                                                <span>$${item.bookPrice}</span>
                                            </div>
                                        </td>                                        
                                    </tr> `;
                    });
                    $("#shopping-cart-items").empty().append(itemHtml);
                }
            }, error: function (errors) {
                console.log(errors);
            }
        });
    };

    loadShoppingCart();

    // setup gridView
    $('#grid').DataTable({
        "autoWidth": true,
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
    });

    $("#subscribeBtn").click(function() {
        let action = $("#subscribeBtn").text();
        let publisherId = $("#publisherId").val();

        $.ajax({
            method: 'POST',
            url: serverUrl + "/customer/subscribe/"+ action + "/" + publisherId,
            dataType: 'json',
            contentType: 'application/json',
            success: function (result) {
                if(result){
                $("#subscribeBtn").html('Unsubscribe');

                } else {
                $("#subscribeBtn").html('Subscribe');

                }

            }, error: function (errors) {
                console.log(errors);
            }
        });
    });

});