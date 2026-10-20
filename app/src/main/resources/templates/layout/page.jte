@import gg.jte.Content
@import hexlet.code.dto.BasePage

@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="/css/main.css" rel="stylesheet" />
    <title>Page Analyse</title>
</head>
<header>
    <nav class="rounded bg-dark">
        <h1 class="text-5xl text-white font-light">Анализатор страниц</h1>
        <a href="/">Главная</a>
        <a href="/urls">Сайты</a>
    </nav>
</header>
<body>
    @if(page.getFlash() != null)
        @if("success".equals(page.getFlashType()))
            <div role="alert" class="rounded border border-green-300 bg-green-50 p-3 text-green-700">
                ${page.getFlash()}
            </div>
        @else
            <div role="alert" class="rounded border border-red-300 bg-red-50 p-3 text-red-700">
                ${page.getFlash()}
            </div>
        @endif
    @endif
    ${content}
</body>
</html>