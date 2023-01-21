
import React, { useState, useEffect } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import CustomerService from "../../services/CustomerService";

const DataTableLazyDemo = () => {

    const [loading, setLoading] = useState(false);
    const [totalRecords, setTotalRecords] = useState(0);
    const [customers, setCustomers] = useState(null);
    const [selectAll, setSelectAll] = useState(false);
    const [selectedCustomers, setSelectedCustomers] = useState(null);
    const [selectedRepresentative, setSelectedRepresentative] = useState(null);
    const [lazyParams, setLazyParams] = useState({
        first: 0,
        rows: 10,
        page: 1,
        sortField: null,
        sortOrder: null,
        filters: {
            'name': { value: '', matchMode: 'contains' },
            'country.name': { value: '', matchMode: 'contains' },
            'company': { value: '', matchMode: 'contains' },
            'representative.name': { value: '', matchMode: 'contains' },
        }
    });

    const customerService = new CustomerService();

    let loadLazyTimeout = null;

    useEffect(() => {
        loadLazyData();
    },[lazyParams]) // eslint-disable-line react-hooks/exhaustive-deps

    const loadLazyData = () => {
        setLoading(true);

        if (loadLazyTimeout) {
            clearTimeout(loadLazyTimeout);
        }

        //imitate delay of a backend call
        loadLazyTimeout = setTimeout(() => {
            customerService.getCustomers({ lazyEvent: JSON.stringify(lazyParams) }).then(data => {
                setTotalRecords(data.totalRecords);
                setCustomers(data.customers);
                setLoading(false);
            });
        }, Math.random() * 1000 + 250);
    }

    const onPage = (event) => {
        setLazyParams(event);
    }

    const onSort = (event) => {
        setLazyParams(event);
    }

    const onFilter = (event) => {
        event['first'] = 0;
        setLazyParams(event);
    }

    const onSelectionChange = (event) => {
        const value = event.value;
        setSelectedCustomers(value);
        setSelectAll(value.length === totalRecords);
    }

    const onSelectAllChange = (event) => {
        const selectAll = event.checked;

        if (selectAll) {
            customerService.getCustomers().then(data => {
                setSelectAll(true);
                setSelectedCustomers(data.customers);
            });
        }
        else {
            setSelectAll(false);
            setSelectedCustomers([]);
        }
    }

    const representativeBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <img alt={rowData.representative.name} src={`images/avatar/${rowData.representative.image}`} onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} width={32} style={{ verticalAlign: 'middle' }} />
                <span className="image-text">{rowData.representative.name}</span>
            </React.Fragment>
        );
    }

    const countryBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <img alt="flag" src="/images/flag/flag_placeholder.png" onError={(e) => e.target.src = 'https://www.primefaces.org/wp-content/uploads/2020/05/placeholder.png'} className={`flag flag-${rowData.country.code}`} width={30} />
                <span className="image-text">{rowData.country.name}</span>
            </React.Fragment>
        );
    }

    return (
        <div>
            <div className="card">
                <DataTable value={customers} lazy filterDisplay="row" responsiveLayout="scroll" dataKey="id"
                           paginator first={lazyParams.first} rows={10} totalRecords={totalRecords} onPage={onPage}
                           onSort={onSort} sortField={lazyParams.sortField} sortOrder={lazyParams.sortOrder}
                           onFilter={onFilter} filters={lazyParams.filters} loading={loading}
                           selection={selectedCustomers} onSelectionChange={onSelectionChange}
                           selectAll={selectAll} onSelectAllChange={onSelectAllChange}>
                    <Column selectionMode="multiple" headerStyle={{ width: '3em' }}></Column>
                    <Column field="name" header="Name" sortable filter filterPlaceholder="Search by name" />
                    <Column field="country.name" sortable header="Country" filterField="country.name" body={countryBodyTemplate} filter filterPlaceholder="Search by country" />
                    <Column field="company" sortable filter header="Company" filterPlaceholder="Search by company" />
                    <Column field="representative.name" header="Representative" body={representativeBodyTemplate} filter filterPlaceholder="Search by representative" />
                </DataTable>
            </div>
        </div>
    );
}

export default DataTableLazyDemo;
