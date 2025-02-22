

const Separacionseccion: React.FC = () => {
    return (
        <div className="mt-8">
            <div className="bg-black-green-gradient h-[190px] md:h-[140px] flex flex-col justify-center items-center md:flex-row space-y-2 md:space-y-0 md:space-x-32">
                <p className="text-white font-sans md:text-2xl">conoce todo lo que tenemos para brindarte</p>
                <hr className="w-full border-green-hover border-2 md:hidden" />
                <div className="hidden md:block w-[2px] h-24 bg-green-hover"></div>
                <div className="flex flex-row justify-center items-center space-x-4">
                    <div className="flex flex-col justify-center items-center">
                      <img src="/payment-icon.svg" alt="payment icon" className="w-12 hover:invert" />
                      <p className="text-white text-sm md:text-xl ">Tarjetas de credito</p>
                    </div>

                    <div className="flex flex-col justify-center items-center">
                      <img src="/signal-icon.svg" alt="payment icon" className="w-12 hover:invert" />
                      <p className="text-white text-sm md:text-xl">Tarjetas de credito</p>
                    </div>

                    <div className="flex flex-col justify-center items-center">
                      <img src="/support-icon.svg" alt="payment icon" className="w-12 hover:invert" />
                      <p className="text-white text-sm md:text-xl">Tarjetas de credito</p>
                    </div>
                </div>

                
            </div>
        </div>
    )
}

export default Separacionseccion