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
    <nav class="rounded bg-gray-900 px-4 py-2">
        <a href="/" class="text-5xl text-white font-light">Анализатор страниц</a>
        <a href="/" class="text-5xl text-white font-light">Главная</a>
        <a href="/urls" class="text-5xl text-white font-light">Сайты</a>
    </nav>
</header>
<body>
    @if(page.getFlash() != null)
        <div role="alert" class="rounded border p-3 ${
                "success".equals(page.getFlashType())
                ? "border-green-300 bg-green-50 text-green-700"
                : "border-red-300 bg-red-50 text-red-700"
                }">
            ${page.getFlash()}
        </div>
    @endif
    ${content}
</body>
</html>