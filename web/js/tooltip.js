// span Tooltips
function initTooltips() {
    $('.tooltip').live('mouseenter',function(e) {
        var offset = $(this).offset();
        var spanwidth = $(this).width();
        $('body').append('<div id="tip">'+$(this).attr('data-tooltip')+'<div class="bubble"></div></div>');
        var w = $('#tip').width();
        var h = $('#tip').height();		
        $('#tip').css({
            top: (offset.top-h-30)+'px',
            left: (offset.left+(spanwidth/2)-(w/2))+'px'
            });
        $('#tip .bubble').css({
            marginLeft: w/2-5+'px'
            });
    });
    $('.tooltip').live('mouseleave',function() {
        $('#tip').remove();
    });
}
$(document).ready(function() {	
    initTooltips();	
});