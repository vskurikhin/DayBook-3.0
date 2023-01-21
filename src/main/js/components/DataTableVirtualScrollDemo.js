import CarService from '../services/CarService';
import React, {useState} from 'react';
import {Column} from 'primereact/column';
import {DataTable} from 'primereact/datatable';
import {Skeleton} from 'primereact/skeleton';

const DataTableVirtualScrollDemo = () => {

    const carService = new CarService();
    const [cars, setCars] = useState(Array.from({length: 100000}).map((_, i) => carService.generateCar(i + 1)));
    const [virtualCars, setVirtualCars] = useState(Array.from({length: 100000}));
    const [lazyLoading, setLazyLoading] = useState(false);
    let loadLazyTimeout = null;

    const loadCarsLazy = (event) => {
        !lazyLoading && setLazyLoading(true);

        if (loadLazyTimeout) {
            clearTimeout(loadLazyTimeout);
        }

        //simulate remote connection with a timeout
        loadLazyTimeout = setTimeout(() => {
            let _virtualCars = [...virtualCars];
            let {first, last} = event;

            console.log("first: " + first + " last: " + last)

            //load data of required page
            const loadedCars = cars.slice(first, last);

            //populate page of virtual cars
            Array.prototype.splice.apply(_virtualCars, [...[first, last - first], ...loadedCars]);

            setVirtualCars(_virtualCars);
            setLazyLoading(false);
        }, Math.random() * 10 + 10);
    }

    const loadingTemplate = (options) => {
        return (
            <div className="flex align-items-center" style={{height: '17px', flexGrow: '1', overflow: 'hidden'}}>
                <Skeleton width={options.cellEven ? (options.field === 'year' ? '30%' : '40%') : '60%'} height="1rem"/>
            </div>
        )
    }

    return (
        <div>
            <div className="card">
                <h5>Lazy Loading from a Remote Datasource (100000 Rows)</h5>
                <DataTable value={virtualCars} scrollable scrollHeight="400px" virtualScrollerOptions={{
                    lazy: true,
                    onLazyLoad: loadCarsLazy,
                    itemSize: 46,
                    delay: 0,
                    showLoader: true,
                    loading: lazyLoading,
                    loadingTemplate
                }}>
                    <Column field="id" header="Id" style={{minWidth: '200px'}}></Column>
                    <Column field="vin" header="Vin" style={{minWidth: '200px'}}></Column>
                    <Column field="year" header="Year" style={{minWidth: '200px'}}></Column>
                    <Column field="brand" header="Brand" style={{minWidth: '200px'}}></Column>
                    <Column field="color" header="Color" style={{minWidth: '200px'}}></Column>
                </DataTable>
            </div>
        </div>
    );
}

export default DataTableVirtualScrollDemo;