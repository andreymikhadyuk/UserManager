jQuery(document).ready(function ($) {
    //разрешаем выводить данные
    var block = true;
    //номер страницы для вывода
    //var page = 0;
    //скроллинг
    $(window).scroll(function () {
        if ( $(window).scrollTop() + 800 >= $(document).height()) {
            block = false;
            //page++;
            //$.get("/search?page=" + page, function(list) {
            $.get("/search", function(list) {
                stopLoading();
                    list = decodeURIComponent(list).replace(/\+/gi, ' ');
                    if (list == null) {
                        block = false;
                    } else {
                        $('.table').append(list);
                        block = true;
                    }
            });
            startLoading();
        }
    });
    //старт анимация ajax
    function startLoading() {
        $('.loading').fadeIn(300);
    }
    //конец анимации ajax
    function stopLoading() {
        $('.loading').fadeOut();
    }
});