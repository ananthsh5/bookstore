$(document).ready(function () {
   let loadCart = function () {
       $.ajax({
           method: 'GET',
           url: '/customer/shoppingCart',
           contentType: 'application/json',
           dataType: 'json',
           success: function (cart) {
               $('#cart-item').html("");
               $.each(cart, function (i, item) {
                   $("#cart-item").append('<tr><td>' + item.bookName + '</td>' +
                   '<td>$' + item.bookPrice + '</td>' +
                   '<td><button class="decrease btn btn-light" data-id="' + item.id + '">-</button> ' + item.quantity + ' <button class="increase btn btn-light" data-id="' + item.id + '">+</button></td>' +
                   '<td><button class="remove-item btn btn-primary" data-id="' + item.id + '">Remove</button></td></tr>');
               });
           },
           error: function () {
               alert('Error while request..');
           }
       });
   };

   loadCart();

   
    $(document).on('click', '.increase', function(){
        var itemId = $(this).data("id");
        $.ajax({
            url: '/customer/cart/' + itemId + '/increaseQuantity',
            type: 'PUT',
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {
                loadCart();
            },
            error: function(error){
                console.log(error);
            }
        });
    });

    $(document).on('click', '.decrease', function(){
        var itemId = $(this).data("id");
        $.ajax({
            url: '/customer/cart/' + itemId + '/decreaseQuantity',
            type: 'PUT',
            contentType: 'application/json',
            dataType: 'text',
            success: function (response) {
                loadCart();
            },
            error: function(error){
                console.log(error);
            }
        });
    });

   $(document).on('click', '.remove-item', function(){
        var itemId = $(this).data("id");
        $.ajax({
            url: '/customer/cart/remove/' + itemId,
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                loadCart();
            },
            error: function(error){
                console.log(error);
            }
        });
   });

});
