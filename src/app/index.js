import "babel-polyfill";
import React from "react";
import {render} from "react-dom";
import configStore from "./store";
import rootSaga from "./sagas";
import Root from "./containers/Root";
import routes, {history} from "./routes";


const store = configStore({});
rootSaga.map(store.runSaga);

render(<Root store={store}
             history={history}
             routes={routes}
    />,
    document.getElementById('app'));