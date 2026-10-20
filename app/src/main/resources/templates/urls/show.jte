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
        <input type="submit" class="cursor-pointer rounded bg-blue-600 px-8 py-2 text-lg font-medium text-white uppercase hover:bg-blue-700" value="Запустить проверку">
    </form>
    <div class="overflow-x-auto">
        <table data-test="checks" class="min-w-full border-collapse border border-gray-300">
            <thead>
                <tr class="bg-gray-50">
                    <th class="text-left font-medium px-4 py-2">ID</th>
                    <th class="text-left font-medium px-4 py-2">Код ответа</th>
                    <th class="text-left font-medium px-4 py-2">h1</th>
                    <th class="text-left font-medium px-4 py-2">title</th>
                    <th class="text-left font-medium px-4 py-2">description</th>
                    <th class="text-left font-medium px-4 py-2">Дата создания</th>
                </tr>
            </thead>
            <tbody>
            @for(var check : page.getChecks())
                <tr class="odd:bg-white even:bg-gray-50 hover:bg-gray-50">
                    <td class="border border-gray-300 px-4 py-2">${check.getId()}</td>
                    <td  class="border border-gray-300 px-4 py-2">${check.getStatusCode()}</td>
                    <td  class="border border-gray-300 px-4 py-2">
                        @if(check.getH1() != null && check.getH1().length() > 200)
                            ${check.getH1().substring(0, 200)}...
                        @else
                            ${check.getH1()}
                        @endif
                    </td>
                    <td  class="border border-gray-300 px-4 py-2">
                        @if(check.getTitle() != null && check.getTitle().length() > 200)
                            ${check.getTitle().substring(0, 200)}...
                        @else
                            ${check.getTitle()}
                        @endif
                    </td>
                    <td  class="border border-gray-300 px-4 py-2">
                        @if(check.getDescription() != null && check.getDescription().length() > 200)
                            ${check.getDescription().substring(0, 200)}...
                        @else
                            ${check.getDescription()}
                        @endif
                    </td>
                    <td  class="border border-gray-300 px-4 py-2">${String.valueOf(check.getCreatedAt())}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)