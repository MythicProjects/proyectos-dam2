import './Header.css'

export default function Header() {
    return (
    <header className='header'>
        <h1 className='logo'>🎮 MisJuegos</h1>
        <nav>
        <a href="#">Inicio</a>
        <a href="#">Juegos</a>
        <a href="#">Contacto</a>
        </nav>
    </header>
    )
}