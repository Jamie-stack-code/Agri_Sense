Add-Type -AssemblyName System.Drawing

$drawablePath = "app\src\main\res\drawable"
$maxDimension = 1024
$quality = 75

$encoder = [System.Drawing.Imaging.ImageCodecInfo]::GetImageEncoders() | Where-Object { $_.MimeType -eq "image/jpeg" }
$encoderParams = New-Object System.Drawing.Imaging.EncoderParameters(1)
$encoderParams.Param[0] = New-Object System.Drawing.Imaging.EncoderParameter([System.Drawing.Imaging.Encoder]::Quality, [long]$quality)

$images = @("img_farmer_produce.jpg", "img_farmer_tablet.jpg", "img_grown_local.jpg", "img_harvest_basket.jpg", "img_john_banda.jpg", "img_limbani_gondwe.jpg", "img_mary_phiri.jpg")

foreach ($imgName in $images) {
    $imgPath = Join-Path $drawablePath $imgName
    $imgPath = Join-Path (Get-Location) $imgPath
    
    if (-not (Test-Path $imgPath)) {
        Write-Host "SKIP: $imgName not found"
        continue
    }
    
    $originalSize = (Get-Item $imgPath).Length
    
    $bmp = New-Object System.Drawing.Bitmap($imgPath)
    $w = $bmp.Width
    $h = $bmp.Height
    
    if ($w -gt $maxDimension -or $h -gt $maxDimension) {
        if ($w -gt $h) {
            $newW = $maxDimension
            $newH = [int]($h * $maxDimension / $w)
        } else {
            $newH = $maxDimension
            $newW = [int]($w * $maxDimension / $h)
        }
    } else {
        $newW = $w
        $newH = $h
    }
    
    $resized = New-Object System.Drawing.Bitmap($newW, $newH)
    $g = [System.Drawing.Graphics]::FromImage($resized)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($bmp, 0, 0, $newW, $newH)
    $g.Dispose()
    $bmp.Dispose()
    
    $tempPath = $imgPath + ".tmp"
    $resized.Save($tempPath, $encoder, $encoderParams)
    $resized.Dispose()
    
    Remove-Item $imgPath -Force
    Rename-Item $tempPath $imgPath
    
    $newSize = (Get-Item $imgPath).Length
    $savingPct = [math]::Round((1 - $newSize / $originalSize) * 100, 1)
    Write-Host "OK: $imgName  ${originalSize} -> ${newSize} bytes  (saved ${savingPct}%)"
}

Write-Host ""
Write-Host "Done! All images optimized."
