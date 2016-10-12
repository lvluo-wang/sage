import "babel-polyfill";
import React from "react";
import {render} from "react-dom";
import configStore from "./store";
import rootSaga from "./sagas";
import Root from "./containers/Root";
import routes, {history} from "./routes";
import LOGIN from "./services";


const store = configStore({});
rootSaga.map(store.runSaga);
LOGIN.store = store;


render(<Root store={store}
             history={history}
             routes={routes}
    />,
    document.getElementById('app'));