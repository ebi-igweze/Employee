$(function(){
    //jquery for the tooltip
    $("[data-toggle='tooltip']").tooltip({animation: true});
    
    
    
    
    //make menus dropdown automatically
	$('ul.nav li.dropdown').hover(function(){

		$('.dropdown-menu', this).fadeIn();

	}, function() {
		$('.dropdown-menu', this).fadeOut('fast');
	});//hover
        
        //for the forms to appear over the page
        $('.modallinks a').on('click', function(){
            $('#modal').modal({
                show: true
            });
        });
        
        //link to benefits tabs
        var hash = window.location.hash;
        hash && $('ul.nav a[href="'+hash+'"]').tab('show');
        
        //display message for remove training or qualification
        $( "#remove" ).click(function() {
  $( "div.warning" ).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
});
});

