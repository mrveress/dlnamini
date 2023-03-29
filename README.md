# dlnamini

This is just sample project for investigating DLNA structure and multicasting stuff.

Thanks [Tobias Aigner](https://github.com/taigner) for his [old project](https://github.com/taigner/simple-streamer) as a start for investigation process and a template sources.

Also I took a free icons from [flaticon.com - DinosoftLabs](https://www.flaticon.com/authors/dinosoftlabs).

This project created to investigate and understand how **Scala/Java** works with **Multicasting, UPnP and Datagram packages**. I was able to start to stream old movie on my old TV, but don't expect from this project more :smiley:

### Prerequisites
So, prerequisites for packaging/running the project:
- installed java (try 19 one)
- installed sbt (latest)
- installed ffmpeg (latest)

### How to run
- Go to terminal
- "cd" to root folder there you cloned this repo
- For package just run
```bash
source package.sh
```
- After that, for running project itself just run
```bash
source run.sh
```