$(document).ready(function () {
   let loadSubscribers = function () {
       $.ajax({
           method: 'GET',
           url: '/customer/subscribers',
           dataType: 'json',
           success: function (publishers) {
               $('#publisher').html("");
               $.each(publishers, function (i, publisher) {
                   $("#publisher").append('<tr><td>' + publisherdto.name + '</td>' +
                   '<td>' + publisherdto.phone + '</td>' +
                   '<td>' + publisherdto.email + '</td>' +
                   '<td>' + publisherdto.address + '</td>' +
                   '<td><button class="unsubscribe btn btn-primary" data-id="' + publisherdto.id + '">Unsubscribe</button></td></tr>');
               });
           },
           error: function () {
               alert('Error while request..');
           }
       });
   };

    loadSubscribers();

   $(document).on('click', '.unsubscribe', function(){
        var publisherId = $(this).data("id");
        $.ajax({
            url: '/customer/subscribe/unsubscribe/' + publisherId,
            type: 'DELETE',
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                loadSubscribers();
            },
            error: function(error){
                console.log(error);
            }
        });
   });

});


