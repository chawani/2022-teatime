import { Routes, Route } from 'react-router-dom';

import Crew from '@pages/Crew';
import Reservation from '@pages/Reservation';
import SelectUser from '@pages/SelectUser';
import Schedule from '@pages/Schedule';
import Coach from '@pages/Coach';
import NotFound from '@pages/NotFound';
import AddSheet from '@pages/AddSheet';
import ViewSheet from '@pages/ViewSheet';
import Header from '@components/Header';
import ScheduleProvider from '@context/ScheduleProvider';
import { ROUTES } from './constants';

const App = () => {
  return (
    <ScheduleProvider>
      <Header />
      <Routes>
        <Route path={ROUTES.HOME} element={<SelectUser />} />
        <Route path={ROUTES.CREW} element={<Crew />} />
        <Route path={`${ROUTES.COACH}/:id`} element={<Coach />} />
        <Route path={`${ROUTES.RESERVATION}/:id`} element={<Reservation />} />
        <Route path={`${ROUTES.SCHEDULE}/:id`} element={<Schedule />} />
        <Route path={`${ROUTES.ADD_SHEET}/:id`} element={<AddSheet />} />
        <Route path={`${ROUTES.VIEW_SHEET}/:id`} element={<ViewSheet />} />
        <Route path="/*" element={<NotFound />} />
      </Routes>
    </ScheduleProvider>
  );
};

export default App;
