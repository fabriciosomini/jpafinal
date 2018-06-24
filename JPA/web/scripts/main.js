(() => {
    $(function () {
        $("[data-toggle=popover]").popover({
            html: true,
            content: function () {
                return $('.popover-body').html();
            },
            title: function () {
                var title = $(this).attr("data-popover-content");
                return $(title).children(".popover-heading").html();
            },
            animation: true
        });
    });


})();