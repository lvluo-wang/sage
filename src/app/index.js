import "babel-polyfill";
import React from "react";
import {render} from "react-dom";
import configStore from "./store";
import rootSaga from "./sagas";
import Root from "./containers/Root";
import createRoutes, {history} from "./routes";
import LOGIN from "./services";


const store = configStore({});
rootSaga.map(store.runSaga);
LOGIN.store = store;

const routes = createRoutes(()=> {
    return store.getState().user.isAdmin;
});


render(<Root store={store}
             history={history}
             routes={routes}
    />,
    document.getElementById('app'));