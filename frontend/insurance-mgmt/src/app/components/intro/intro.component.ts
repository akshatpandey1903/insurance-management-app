import { AfterViewInit, Component , ElementRef, Inject, PLATFORM_ID , Renderer2, ViewChild} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-intro',
  standalone: false,
  templateUrl: './intro.component.html',
  styleUrl: './intro.component.css'
})
export class IntroComponent implements AfterViewInit {

  @ViewChild('carousel') carousel: ElementRef | undefined;


  private isBrowser: boolean = false;
  private intervalId: any;
  private currentIndex = 0;
  private slides: HTMLElement[] = [];
  private dots: HTMLElement[] = [];

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private renderer: Renderer2
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  testimonials = [
    { avatar: 'https://randomuser.me/api/portraits/men/32.jpg', text: 'ClaimSure LIC made my policy purchase and claims process extremely smooth. Highly recommended!', stars: '⭐⭐⭐⭐⭐', name: 'Rajesh Kumar' },
    { avatar: 'https://randomuser.me/api/portraits/women/45.jpg', text: 'I had an issue with my claim, but their 24/7 support resolved it quickly. Great service!', stars: '⭐⭐⭐⭐', name: 'Anjali Mehta' },
    { avatar: 'https://randomuser.me/api/portraits/men/58.jpg', text: 'Transparent, efficient, and reliable. ClaimSure LIC is the future of insurance!', stars: '⭐⭐⭐⭐⭐', name: 'Vikram Sharma' },
    { avatar: 'https://randomuser.me/api/portraits/women/65.jpg', text: 'I switched to ClaimSure LIC after years with another company. Best decision ever!', stars: '⭐⭐⭐⭐⭐', name: 'Pooja Desai' },
    { avatar: 'https://randomuser.me/api/portraits/men/77.jpg', text: 'The app is easy to use, and policy documents are just a click away.', stars: '⭐⭐⭐⭐', name: 'Amit Tiwari' },
    { avatar: 'https://randomuser.me/api/portraits/women/36.jpg', text: 'My claim was processed within 3 days. No unnecessary paperwork or delays!', stars: '⭐⭐⭐⭐⭐', name: 'Sneha Kulkarni' },
    { avatar: 'https://randomuser.me/api/portraits/men/21.jpg', text: 'Highly professional team with excellent customer service.', stars: '⭐⭐⭐⭐', name: 'Rohan Patil' },
    { avatar: 'https://randomuser.me/api/portraits/women/52.jpg', text: 'I love the transparency. Everything is crystal clear with ClaimSure LIC.', stars: '⭐⭐⭐⭐⭐', name: 'Divya Nair' }
  ];

  ngAfterViewInit(): void {
    if (this.isBrowser) {
      this.slides = Array.from(document.querySelectorAll('.slide-img'));
      this.dots = Array.from(document.querySelectorAll('.dot'));

      this.showSlide(this.currentIndex);
      this.setupDotClicks();
      this.startAutoSlide();
    }
  }

  showSlide(index: number): void {
    this.slides.forEach((slide, i) => {
      slide.classList.remove('active');
    });
    this.dots.forEach((dot, i) => {
      dot.classList.remove('active-dot');
    });

    this.slides[index].classList.add('active');
    this.dots[index].classList.add('active-dot');
    this.currentIndex = index;
  }

  setupDotClicks(): void {
    this.dots.forEach((dot, i) => {
      dot.addEventListener('click', () => {
        this.showSlide(i);
        this.restartAutoSlide();
      });
    });
  }

  startAutoSlide(): void {
    this.intervalId = setInterval(() => {
      const nextIndex = (this.currentIndex + 1) % this.slides.length;
      this.showSlide(nextIndex);
    }, 5000);
  }

  restartAutoSlide(): void {
    clearInterval(this.intervalId);
    this.startAutoSlide();
  }

}
