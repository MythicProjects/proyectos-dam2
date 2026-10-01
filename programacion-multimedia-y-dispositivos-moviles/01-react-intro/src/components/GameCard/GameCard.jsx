import { useState } from 'react'
import './GameCard.css'
import emptyHeart from '../../assets/hearts/heart_empty.png'
import fullHeart from '../../assets/hearts/heart.png'

export default function GameCard({title, imageUrl, platform, year}){
    const [like, setLike] = useState(false);

    return(
        <>
            <div className='gameCard'>
                <div className='gameImage'>
                    <img src={imageUrl} alt={title} />
                </div>

                <div className='gameInfo'>
                    <h2>{title}</h2>
                    <h3>{platform}</h3>
                    <p>{year}</p>
                </div>

                <div className='gameLike'>
                    <button onClick={() => setLike(!like)}>
                        <img
                            className={like ? "liked" : "disliked"} 
                            src={like ? fullHeart : emptyHeart} alt="" />
                    </button>
                </div>
            </div>
        </>
    )
}