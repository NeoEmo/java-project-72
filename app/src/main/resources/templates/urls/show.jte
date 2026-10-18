@import hexlet.code.dto.UrlPage

@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <table data-test="url">
        <tbody>
        <tr>
            <td>ID</td>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <td>Имя</td>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <td>Дата создания</td>
            <td>${page.getUrl().getCreatedAt().toString()}</td>
        </tr>
        </tbody>
    </table>
    <form method="post" action="/urls/${page.getUrl().getId()}/checks">
        <input type="submit" value="Запустить проверку">
    </form>

    <table data-test="checks">
        <thead>
        <tr>
            <th>ID</th>
            <th>Код ответа</th>
            <th>h1</th>
            <th>title</th>
            <th>description</th>
            <th>Дата создания</th>
        </tr>
        </thead>
        <tbody>
        @for(var check : page.getChecks())
            <tr>
                <td>${check.getId()}</td>
                <td>${check.getStatusCode()}</td>
                <td>
                    @if(check.getH1() != null && check.getH1().length() > 200)
                        ${check.getH1().substring(0, 200)}...
                    @else
                        ${check.getH1()}
                    @endif
                </td>
                <td>
                    @if(check.getTitle() != null && check.getTitle().length() > 200)
                        ${check.getTitle().substring(0, 200)}...
                    @else
                        ${check.getTitle()}
                    @endif
                </td>
                <td>
                    @if(check.getDescription() != null && check.getDescription().length() > 200)
                        ${check.getDescription().substring(0, 200)}...
                    @else
                        ${check.getDescription()}
                    @endif
                </td>
                <td>${String.valueOf(check.getCreatedAt())}</td>
            </tr>
        @endfor
        </tbody>
    </table>
`
)