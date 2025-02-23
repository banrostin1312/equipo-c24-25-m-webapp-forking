//Components
import Swipercarousel from "./components/Carousel/carousel";
import Middleinfo from "./components/Middleinfo/Middleinfo";
import Separacionseccion from "./components/Separacionseccion/Separacionseccion";
import VideoPromotion from "./components/Video/VideoPromotion";
export default function Home() {
  return (
   <div>
  
    <Swipercarousel/>
    <Middleinfo/>
    <Separacionseccion/>
    <VideoPromotion/>
   </div>
    
  );
}
