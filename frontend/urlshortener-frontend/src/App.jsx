import { useMemo, useState } from 'react'
import './App.css'
import GlassSurface from './blocks/Components/GlassSurface/GlassSurface'
import Galaxy from './blocks/Backgrounds/Galaxy/Galaxy'
function App() {
  const [url, setUrl] = useState('')
  const [shortenedUrl, setShortenedUrl] = useState('')

  // Keep Galaxy's array props stable across renders to avoid teardown on typing
  const galaxyFocal = useMemo(() => [0.5, 0.5], [])
  const galaxyRotation = useMemo(() => [1.0, 0.0], [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    const isDev = import.meta.env.DEV
    const API_BASE_URL = isDev
      ? '' // use Vite proxy in dev
      : (import.meta.env.VITE_API_BASE_URL || '')
    try {
      const response = await fetch(`${API_BASE_URL}/api/shorten`, {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain;charset=UTF-8',
        },
        body: url.trim(),
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(errorText || `Request failed with ${response.status}`)
      }

      const shortened = (await response.text()).trim()
      setShortenedUrl(shortened)
    } catch (err) {
      console.error('Shorten request failed:', err)
      alert('Failed to shorten URL. See console for details.')
    }
  }

  return (
    <div className="min-h-screen relative bg-black overflow-hidden">
      <div className="absolute inset-0">
        <Galaxy 
          focal={galaxyFocal}
          rotation={galaxyRotation}
          mouseRepulsion={true}
          mouseInteraction={true}
          density={2}
          glowIntensity={0.3}
          saturation={0.6}
          hueShift={240}
          transparent={true}
        />
      </div>

      <div className="relative flex items-center justify-center min-h-screen">
        <GlassSurface 
          width={400} 
          height="auto"
          borderRadius={20}
          brightness={60}
          opacity={0.8}
          blur={12}
          displace={5}
          distortionScale={-150}
          redOffset={5}
          greenOffset={15}
          blueOffset={25}
          className="p-8 w-full max-w-md"
        >
          <div className="w-full">
            <h1 className="font-bold text-center mb-6 text-white">Shorten URL</h1>
            
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label htmlFor="url" className="block text-sm font-medium text-gray-200 mb-1">
                  Paste long URL here
                </label>
                <input
                  type="text"
                  id="url"
                  value={url}
                  onChange={(e) => setUrl(e.target.value)}
                  className="w-full text-white px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-slate-200"
                  placeholder="https://example.com/very/long/url"
                  required
                />
              </div>
              
              <button
                type="submit"
                className="w-full bg-slate-600 text-white py-2 px-4 rounded-md hover:bg-slate-200 focus:outline-none focus:ring-2 focus:ring-slate-200 focus:ring-offset-2"
              >
                Shorten
              </button>
            </form>
            
            {shortenedUrl && (
              <div className="mt-6 p-4 bg-gray-900 bg-opacity-50 rounded-md">
                <p className="text-sm text-gray-300 mb-1">Your shortened URL:</p>
                <a 
                  href={shortenedUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-slate-400 font-medium break-all"
                >
                  {shortenedUrl}
                </a>
              </div>
            )}
          </div>
        </GlassSurface>
      </div>
    </div>
  )
}

export default App
