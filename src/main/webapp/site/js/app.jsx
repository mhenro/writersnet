/**
 * Created by mhenr on 02.10.2017.
 */
import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import MainPage from './pages/MainPage.jsx';
import GlobalDataContainer from './components/GlobalDataContainer.jsx';

import appReducer from './reducers/appReducer.jsx';

let store = createStore(appReducer);

class Root extends React.Component {
    render() {
        return (
            <MainPage />
        )
    }
}

render(
    <Provider store={store}>
        <div>
            <Root />
            <GlobalDataContainer/>
        </div>
    </Provider>,
    document.getElementById('content')
);