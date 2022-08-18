import { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { UserStateContext } from '@context/UserProvider';
import * as S from './styles';
import SlackIcon from '@assets/slack.svg';

const Home = () => {
  const navigate = useNavigate();
  const { userData } = useContext(UserStateContext);

  const handleLogin = () => {
    location.href =
      'https://slack.com/openid/connect/authorize?response_type=code&scope=openid%20profile%20email&client_id=3853132979991.3908459157459&redirect_uri=https://teatime.pe.kr/certification';
  };

  useEffect(() => {
    if (userData) {
      navigate(`/${userData.role.toLowerCase()}`);
    }
  }, [userData]);

  return (
    <S.Login>
      <S.SlackLoginButton onClick={handleLogin}>
        <img src={SlackIcon} />
        <span>Slack 로그인</span>
      </S.SlackLoginButton>
    </S.Login>
  );
};

export default Home;
