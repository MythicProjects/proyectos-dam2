import './ProfileCard.css'

function ProfileCard(){
    const name = "Mateu Bennasar Lendinez"
    const course = "Desarrollo de aplicaciones multimedia"
    const info = "Me gusta la pizza y las hierbas dulces"
    
    return(
        <>
            <div className='card'>
                <h1>{name}</h1>
                <h3>{course}</h3>
                <p>{info}</p>
            </div>
        </>
    )
}
function ProfileCardWithParameters({name, course, info}){    
    return(
        <>
            <div className='card'>
                <h1>{name}</h1>
                <h3>{course}</h3>
                <p>{info}</p>
            </div>
        </>
    )
}

export default ProfileCardWithParameters