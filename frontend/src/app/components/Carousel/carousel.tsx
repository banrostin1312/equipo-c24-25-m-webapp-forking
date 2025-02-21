"use client"
//Libraries 
//Assets
import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";

const Swipercarousel: React.FC = () => {
    return (
        <div className="flex justify-center items-center mt-28"> 
            <Swiper spaceBetween={10} slidesPerView={1} loop={true} className="md:w-[1200px] md:h-[300px] rounded-2xl">
                <SwiperSlide><Image alt="" src="/carousel-1.png" objectFit="cover" layout="fill"></Image></SwiperSlide>
                <SwiperSlide><Image height={1200} width={300} src={} alt=""></Image></SwiperSlide>
                <SwiperSlide><Image height={1200} width={300} src={} alt=""></Image></SwiperSlide>
            </Swiper>
        </div>
    )
}

export default Swipercarousel