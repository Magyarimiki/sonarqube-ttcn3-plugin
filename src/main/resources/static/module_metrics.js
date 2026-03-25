const metrics = new Map([
    [ 'NOF_STATEMENTS', 'Number of statements' ],
    [ 'NOF_ALTSTEPS', 'Number of altsteps' ],
    [ 'NOF_TESTCASES', 'Number of test cases' ],
    [ 'IN_ENVY', 'Internal feature envy' ],
    [ 'OUT_ENVY', 'External feature envy' ],
    [ 'NOF_FUNCTIONS', 'Number of functions' ],

    // This is broken in Titanium right now
    // [ 'NOF_FIXME', 'Fixme comments' ],
    
    [ 'TIMES_IMPORTED', 'Times imported' ],
    [ 'NOF_IMPORTS', 'Number of imports' ],
    [ 'EFFERENT_COUPLING', 'External assignment references' ],
    [ 'AFFERENT_COUPLING', 'Internal assignment references' ],
    [ 'INSTABILITY', 'Efferent to (efferent plus afferent) coupling ratio' ],
    [ 'LINES_OF_CODE', 'Lines of code' ]
]);

function getProjectKey() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id') || params.get('project');
}

async function loadMetrics() {
    const projectKey = getProjectKey();

    const res = await fetch(
        `/api/measures/component_tree?component=${projectKey}&metricKeys=${[...metrics.keys()].join(',')}&qualifiers=FIL`
    );

    const data = await res.json();

    const table = renderTable(data.components || []);
    return table;
}

function renderTable(components) {
    const container = document.createElement('div');
    container.style.padding = '12px';

    const title = document.createElement('h3');
    title.textContent = 'TTCN3 Module Metrics';
    title.style.marginBottom = '10px';
    container.appendChild(title);

    const table = document.createElement('table');
    table.style.borderCollapse = 'collapse';
    table.style.width = '30%';
    table.style.fontFamily = 'sans-serif';

    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');

    [ 'Metric', 'Value' ].forEach(text => {
        const th = document.createElement('th');
        th.textContent = text;
        th.style.borderBottom = '2px solid #ccc';
        th.style.textAlign = 'left';
        th.style.padding = '8px';
        th.style.background = '#f5f5f5';
        headerRow.appendChild(th);
    });

    thead.appendChild(headerRow);
    table.appendChild(thead);

    const tbody = document.createElement('tbody');
    
    components.forEach(c => {
        if (c.measures.length === 0) {
            return;
        }
        const fileName = document.createElement('td');
        fileName.textContent = c.path;
        fileName.colSpan = 2;
        fileName.style.fontWeight = 'bold';
        fileName.style.paddingTop = '10px';
        const filerow = document.createElement('tr');
        filerow.appendChild(fileName);
        tbody.appendChild(filerow);

        c.measures.forEach((measure, index) => {
            const value = measure.value || '0';
            const number = Number(value);
            if (isNaN(number)) {
                return;
            }

            const row = document.createElement('tr');
            row.style.background = index % 2 === 0 ? '#fff' : '#b9b4b4';
            row.style.paddingLeft = '10px';
            row.style.paddingTop = '3px';

            const measureCell = document.createElement('td');
            measureCell.textContent = metrics.get(measure.metric);

            const valueCell = document.createElement('td');
            valueCell.textContent = value;
            valueCell.style.fontWeight = 'bold';

            row.appendChild(measureCell);
            row.appendChild(valueCell);

            tbody.appendChild(row);
        });
    });

    table.appendChild(tbody);
    container.appendChild(table);

    return container.outerHTML;
}

window.registerExtension('ttcn3/module_metrics', async function (options) {
    options.el.innerHTML = `<h2>Loading</h2>`;
    const container = await loadMetrics();
    options.el.innerHTML = container;
    return;
});