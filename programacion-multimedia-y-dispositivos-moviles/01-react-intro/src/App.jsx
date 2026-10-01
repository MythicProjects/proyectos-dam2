import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import ProfileCardWithParameters from './components/ProfileCard/ProfileCard'
import GameCard from './components/GameCard/GameCard'
import Header from './components/Header/Header'
import Footer from './components/Footer/Footer'

function App() {
  const [count, setCount] = useState(0)
  const name = "Mateu Botó Coll"
  const games = [
    {
      title: "GTA San Andreas",
      imageUrl: "https://upload.wikimedia.org/wikipedia/en/c/c4/GTASABOX.jpg",
      platform: "PlayStation 2",
      year: "2004",
    },
    {
      title: "Ori and the Blind Forest",
      imageUrl: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fassetsio.reedpopcdn.com%2Fco2s0m.jpg%3Fwidth%3D1920%26height%3D1920%26fit%3Dbounds%26quality%3D80%26format%3Djpg%26auto%3Dwebp&f=1&nofb=1&ipt=a8ad9a4defdc4eeec320e2327335c9e415208cdf75ac637b21617472bf816b8e",
      platform: "Xbox One / PC",
      year: "2015",
    },
    {
      title: "Super Mario Odyssey",
      imageUrl: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fstatic1.thegamerimages.com%2Fwordpress%2Fwp-content%2Fuploads%2Fsharedimages%2F2025%2F02%2Fsuper-mario-odyssey-cover-art.jpg&f=1&nofb=1&ipt=63223c37fd98f4543c1a54bd55bcfd3b6f962207f25dda63675cf96ff60fce58",
      platform: "Nintendo Switch",
      year: "2017",
    },
    {
      title: "God of War:Ragnarok",
      imageUrl: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fstatic0.gamerantimages.com%2Fwordpress%2Fwp-content%2Fuploads%2F2024%2F12%2Fmixcollage-08-dec-2024-01-22-pm-530.jpg&f=1&nofb=1&ipt=c77ed584a4bfbef3feeba16c8669b2a462607017d74f69470af1145fe44a1d91",
      platform: "PlayStation 5",
      year: "2020",
    },
    {
      title: "Minecraft",
      imageUrl: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.pinimg.com%2Foriginals%2Fe6%2F86%2Ff1%2Fe686f150ab642a2caf181821679e5797.png&f=1&nofb=1&ipt=9da15802b476b46b2d5c3128a4918b469c14d330e252f20cf04b088ff35b7c68",
      platform: "PC",
      year: "2011",
    },
  ];

  return (
    <>
      <Header />
      <div className='main'>
      {/* 
        <ProfileCardWithParameters 
          name="Mateu Bennasar Lendinez"
          course="Desarrollo de aplicaciones multimedia"
          info="Me gusta la pizza y las hierbas dulces"
        />
        <ProfileCardWithParameters 
          name="Maciá Toni Calantell"
          course="Perruquería"
          info="Me gustan los olivos y el campo"
        />
        <ProfileCardWithParameters 
          name="Josep Toni Monserrat"
          course="Carpintería"
          info="Me gusta la madera de roble roja"
        />
        */}

        {games.map((game) => (
          <GameCard
            key={game.title}
            title={game.title}
            imageUrl={game.imageUrl}
            platform={game.platform}
            year={game.year}
          />
        ))}
      </div>
      <Footer />
    </>
  )
}

export default App
