//CSS
import "./Middle.css"
//Assets
import Image from "next/image"



const Middleinfo: React.FC = () => {
  return (

    <div>
      <section className="mt-12 md:space-y-20">
        <div className="flex flex-col md:flex-row justify-center items-center space-x-4 md:space-x-32 ">
          <Image height={126} width={126} src="/persona3.png" alt="img1 middle" className="md:w-[226px] md:h-[226px]"></Image>
          <div className="flex flex-col justify-center items-center space-y-3 md:flex-row ">
            <div className="flex flex-col justify-center items-center space-y-3">
              <h2 className="text-xl text-middle-title md:text-4xl md:mr-28">Paquete de productos</h2>
              <p className="w-full max-w-md text-center md:text-left text-sm md:text-xl">Optimizá tus recursos, reducí costos y llevá tu negocio o emprendimiento al siguiente nivel. Descubrí soluciones eficientes que te ayudarán a maximizar tu rentabilidad y hacer crecer tu proyecto de manera inteligente.</p>
            </div>
            <div className="flex md:flex-row justify-center items-center space-x-0.5 md:space-x-1 text-middle-title">
            <button className="animated-button">
                <svg viewBox="0 0 24 24" className="arr-2" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M16.1716 10.9999L10.8076 5.63589L12.2218 4.22168L20 11.9999L12.2218 19.778L10.8076 18.3638L16.1716 12.9999H4V10.9999H16.1716Z"
                  ></path>
                </svg>
                <span className="text">Conocer mas</span>
                <span className="circle"></span>
                <svg viewBox="0 0 24 24" className="arr-1" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M16.1716 10.9999L10.8076 5.63589L12.2218 4.22168L20 11.9999L12.2218 19.778L10.8076 18.3638L16.1716 12.9999H4V10.9999H16.1716Z"
                  ></path>
                </svg>
              </button>
            </div>
          </div>

        </div>

        <div className="flex flex-col md:flex-row justify-center items-center space-x-4 md:space-x-32 mt-9">
          <Image height={126} width={126} src="/persona4.png" alt="img1 middle" className="md:w-[226px] md:h-[226px]"></Image>
          <div className="flex flex-col justify-center items-center space-y-3 md:flex-row ">
            <div className="flex flex-col justify-center items-center space-y-3">
              <h2 className="text-xl text-middle-title md:text-4xl md:mr-28">¿Cómo evitar estafas?</h2>
              <p className="w-full max-w-md text-center md:text-left text-sm md:text-xl">Optimizá tus recursos, reducí costos y llevá tu negocio o emprendimiento al siguiente nivel. Descubrí soluciones eficientes que te ayudarán a maximizar tu rentabilidad y hacer crecer tu proyecto de manera inteligente.</p>
            </div>
            <div className="flex md:flex-row justify-center items-center space-x-0.5 md:space-x-1 text-middle-title">
              <button className="animated-button">
                <svg viewBox="0 0 24 24" className="arr-2" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M16.1716 10.9999L10.8076 5.63589L12.2218 4.22168L20 11.9999L12.2218 19.778L10.8076 18.3638L16.1716 12.9999H4V10.9999H16.1716Z"
                  ></path>
                </svg>
                <span className="text">Conocer mas</span>
                <span className="circle"></span>
                <svg viewBox="0 0 24 24" className="arr-1" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M16.1716 10.9999L10.8076 5.63589L12.2218 4.22168L20 11.9999L12.2218 19.778L10.8076 18.3638L16.1716 12.9999H4V10.9999H16.1716Z"
                  ></path>
                </svg>
              </button>

            </div>
          </div>

        </div>

      </section>
    </div>
  )
}

export default Middleinfo