import "babel-polyfill";
import React from "react";
import {render} from "react-dom";
import configStore from "./store";
import rootSaga from "./sagas";
import Root from "./containers/Root";
import createRoutes, {history} from "./routes";
import LOGIN from "./services";
import en_US from "./intl/en_US";
import {IntlProvider} from "react-intl";


const store = configStore({});
rootSaga.map(store.runSaga);
LOGIN.store = store;

const routes = createRoutes(()=> {
    return store.getState().user.isAdmin;
});


render(<IntlProvider locale={'en'}
                     messages={en_US}>
        <Root store={store}
              history={history}
              routes={routes}
              locales={['en-US']}
        />
    </IntlProvider>,
    document.getElementById('app'));