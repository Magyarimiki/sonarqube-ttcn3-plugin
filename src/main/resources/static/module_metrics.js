const uiPageSize = 10;
let uiCurrentPage = 1;
let totalPages = 0;
let allComponents = [];
let handlers = false;

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

const container = document.createElement('div');
container.style.padding = '12px';
container.textAlign = 'center';

const titleContainer = document.createElement('div');
const title = document.createElement('h3');
title.textContent = 'TTCN3 Module Metrics';
title.style.marginBottom = '10px';
container.appendChild(titleContainer);
const buttonBack = document.createElement('button');
buttonBack.paddingLeft = '5px';
setButtonStyle(buttonBack);
buttonBack.textContent = ' < ';
const buttonForward = document.createElement('button');
setButtonStyle(buttonForward);
buttonForward.textContent = ' > ';
const pageText = document.createTextNode('');
const buttonContainer = document.createElement('span');
buttonContainer.appendChild(buttonBack);
buttonContainer.appendChild(pageText);
buttonContainer.appendChild(buttonForward);
container.appendChild(titleContainer);

const table = document.createElement('table');
table.style.borderCollapse = 'collapse';
table.style.width = '800px';
table.style.maxWidth = '100%';
table.style.fontFamily = 'sans-serif';
table.style.overflow = 'auto';
table.style.flex = '1';
const thead = document.createElement('thead');
const headerRow = document.createElement('tr');

[ 'Path / metric ', 'Value' ].forEach((text, index) => {
    const th = document.createElement('th');
    if (index === 0) {
        const thWithButtons = document.createElement('span');
        const label = document.createTextNode(text);
        label.paddingLeft = '5px';
        thWithButtons.appendChild(label);
        thWithButtons.appendChild(buttonContainer);
        th.appendChild(thWithButtons);
    } else {
        th.textContent = text;
    }
    th.style.borderBottom = '2px solid #ccc';
    th.style.textAlign = 'left';
    th.style.padding = '8px';
    th.style.background = '#f5f5f5';
    th.style.zIndex = '100';
    th.style.top = '0';
    th.style.position = 'sticky';
    headerRow.appendChild(th);
});

thead.appendChild(headerRow);
table.appendChild(thead);
const tbody = document.createElement('tbody');
table.appendChild(tbody);
const tableContainer = document.createElement('div');
tableContainer.style.overflow = 'auto';
tableContainer.style.flex = '1';
tableContainer.style.display = 'flex';
tableContainer.style.flexDirection = 'column';
tableContainer.style.alignItems = 'center';
tableContainer.style.height = '70vh';
tableContainer.style.borderBottom = '2px solid #000';
tableContainer.appendChild(table);
container.appendChild(tableContainer);

const metricDescriptions = new Map([
    [ 'NOF_STATEMENTS', 'Number of statements in the module' ],
    [ 'NOF_ALTSTEPS', 'Number of altsteps in the module' ],
    [ 'NOF_TESTCASES', 'Number of test cases in the module' ],
    [ 'IN_ENVY', 'Number of references to entities inside the module' ],
    [ 'OUT_ENVY', 'Number of references to entities outside the module' ],
    [ 'NOF_FUNCTIONS', 'Number of functions in the module' ],

    // This is broken in Titanium right now
    // [ 'NOF_FIXME', 'Number of comments beginning with "FIXME"' ],
    
    [ 'TIMES_IMPORTED', 'Times the module was imported by other modules' ],
    [ 'NOF_IMPORTS', 'Count of module importations in the module' ],
    [ 'EFFERENT_COUPLING', 'Number of referred assignments that are defined outside the module' ],
    [ 'AFFERENT_COUPLING', 'Number of referred assignments that are defined inside the module' ],
    [ 'INSTABILITY', 'Efferent to (efferent plus afferent) coupling ratio' ],
    [ 'LINES_OF_CODE', 'Number of code lines in the module' ]
].sort((a, b) => a < b));

function getProjectKey() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id') || params.get('project');
}

async function loadMetrics() {
    let p = 1;
    const ps = 100;
    const projectKey = getProjectKey();
    let count = 0;

    do {
        const res = await fetch(
            `/api/measures/component_tree?component=${projectKey}&metricKeys=${[...metrics.keys()].join(',')}&qualifiers=FIL&p=${p}&ps=${ps}`
        );

        const data = await res.json();
        allComponents = allComponents.concat(
            data.components.filter(comp => comp.language === 'ttcn3')
        );

        total = data.paging.total;
        count += ps;
        p++;
    } while (count < total);
    totalPages = Math.ceil(total / uiPageSize);
}

function renderTable(components) {
    tbody.innerHTML = '';

    components.forEach(c => {
        const fileName = document.createElement('td');
        fileName.textContent = c.path;
        fileName.colSpan = 2;
        fileName.style.fontWeight = 'bold';
        fileName.style.paddingTop = '10px';
        const filerow = document.createElement('tr');
        filerow.appendChild(fileName);
        tbody.appendChild(filerow);
        if (c.measures.length === 0) {
            const row = document.createElement('tr');
            row.style.background = '#fff';
            row.style.paddingLeft = '10px';
            row.style.paddingTop = '3px';
            const measureCell = document.createElement('td');
            measureCell.colSpan = 2;
            measureCell.style.color = '#ff7a41';
            measureCell.textContent = 'No measures available';
            row.appendChild(measureCell);
            tbody.appendChild(row);
            return;
        }

        c.measures.forEach((measure, index) => {
            const value = measure.value || '0';
            const number = Number(value);
            if (isNaN(number)) {
                return;
            }

            const row = document.createElement('tr');
            row.style.background = index % 2 === 0 ? '#fff' : '#ded8d8';
            row.style.paddingLeft = '10px';
            row.style.paddingTop = '3px';

            const measureCell = document.createElement('td');
            measureCell.textContent = metrics.get(measure.metric);
            const measureDesc = metricDescriptions.get(measure.metric);
            if (measureDesc) {
                measureCell.title = measureDesc;
            }

            const valueCell = document.createElement('td');
            valueCell.textContent = value;
            valueCell.style.fontWeight = 'bold';

            row.appendChild(measureCell);
            row.appendChild(valueCell);

            tbody.appendChild(row);
        });
    });
    
    pageText.textContent = ` page ${uiCurrentPage}/${totalPages} `;
    return container.outerHTML;
}

function getComponentPage() {
    const first = (uiCurrentPage - 1) * uiPageSize;
    return allComponents.slice(first, first + uiPageSize);
}

function setButtonStyle(button) {
    button.style.background = "#0052CC";
    button.style.color = "white";
    button.style.border = "none";
    button.style.padding = "6px 12px";
    button.style.borderRadius = "4px";
    button.style.cursor = "pointer";

    button.onmouseenter = () => button.style.background = "#0065FF";
    button.onmouseleave = () => button.style.background = "#0052CC";
}

function setPagingButtonState() {
    buttonBack.disabled = uiCurrentPage === 1;
    buttonForward.disabled = uiCurrentPage === totalPages;
}

window.registerExtension('ttcn3/module_metrics', async function (options) {
    options.el.innerHTML = `<h2>Loading</h2>`;
    await loadMetrics();
    if (allComponents.length === 0) {
        options.el.innerHTML = `<h2>No measures available (missing analysis or non-TTCN3 project)</h2>`;
        return;
    }
    if (!handlers) {
        handlers = true;
        buttonForward.addEventListener('click', () => {
            if (uiCurrentPage < totalPages) {
                uiCurrentPage++;
                renderTable(getComponentPage() || []);        
            }
            setPagingButtonState();
        });
        buttonBack.addEventListener('click', () => {
            if (uiCurrentPage > 1) {
                uiCurrentPage--;
                renderTable(getComponentPage() || []);        
            }
            setPagingButtonState();
        });
    }
    renderTable(getComponentPage() || []);
    
    options.el.innerHTML = '';
    options.el.appendChild(container);
    return;
});
