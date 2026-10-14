@import hexlet.code.dto.UrlPage
@import gg.jte.Content

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
`
)