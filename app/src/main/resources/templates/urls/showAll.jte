@import hexlet.code.dto.UrlsPage

@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <h1>Сайты</h1>
    <div class="overflow-x-auto">
        <table data-test="urls" class="min-w-full border-collapse border border-gray-300">
            <thead>
                <tr class="bg-gray-50">
                    <th class="text-left font-medium px-4 py-2">ID</th>
                    <th class="text-left font-medium px-4 py-2">Имя</th>
                    <th class="text-left font-medium px-4 py-2">Дата создания</th>
                </tr>
            </thead>
            <tbody>
                @for(var url : page.getUrls())
                    <tr class="odd:bg-white even:bg-gray-50 hover:bg-gray-50">
                        <td class="border border-gray-300 px-4 py-2">${url.getId()}</td>
                        <td class="border border-gray-300 px-4 py-2"><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        <td class="border border-gray-300 px-4 py-2">${url.getCreatedAt().toString()}</td>
                    </tr>
                @endfor
            </tbody>
        </table>
    </div>

`
)